
package cn.com.tiza.service.jobs;

import cn.com.tiza.Global;
import cn.com.tiza.config.ApplicationProperties;
import cn.com.tiza.constant.ExportTaskStatusEnum;
import cn.com.tiza.dao.ExportTaskDao;
import cn.com.tiza.dao.ExportTaskVehicleDao;
import cn.com.tiza.domain.ExportTask;
import cn.com.tiza.domain.ExportTaskVehicle;
import cn.com.tiza.dto.RestDataRecord;
import cn.com.tiza.service.PlatformService;
import com.google.common.collect.Lists;
import com.vip.vjtools.vjkit.time.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * @author TZ0836
 * <p>
 * 根据任务生成转发数据压缩文件
 */

@Slf4j
@Component
public class ForwardDataCompressJob {
    private static final String BACKSLASH = "/";
    private static final String COMPRESS_FILE_SUFFIX = ".zip";

    @Autowired
    private ExportTaskDao exportTaskDao;

    @Autowired
    private ExportTaskVehicleDao exportTaskVehicleDao;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private ApplicationProperties properties;

    public void executeInternal() {
        log.info("run forwardDataCompress job!");
        ExportTask exportTask = new ExportTask();
        exportTask.setStatus(ExportTaskStatusEnum.NOT_PROCESS.getValue());
        List<ExportTask> exportTaskList = exportTaskDao.template(exportTask);
        if (!exportTaskList.isEmpty()) {
            for (ExportTask task : exportTaskList) {
                if (task.getDataType() == null || task.getBeginTime() == null || task.getEndTime() == null
                        || task.getFwpTaskId() == null) {
                    task.setStatus(ExportTaskStatusEnum.PROCESS_EXCEPTION.getValue());
                    exportTaskDao.updateById(task);
                    continue;
                }
                ExportTaskVehicle taskVehicle = new ExportTaskVehicle();
                taskVehicle.setExportTaskId(task.getId());
                List<ExportTaskVehicle> taskVehicleList = exportTaskVehicleDao.template(taskVehicle);
                if (!taskVehicleList.isEmpty()) {
                    task.setStatus(ExportTaskStatusEnum.PROCESSING.getValue());
                    exportTaskDao.updateById(task);
                    String fileDir = properties.getForwardData().getDirectory() + task.getFwpTaskId().toString() + "-" + System.currentTimeMillis() + File.separator;
                    log.debug("export data to dir : {}", fileDir);
                    try {
                        task.setFilePath(this.getIp() + "|"
                                + this.generatedData(task.getBeginTime(), task.getEndTime(), taskVehicleList, fileDir,
                                task.getFwpTaskId(), task.getDataType()));
                        task.setStatus(ExportTaskStatusEnum.PROCESS_SUCCESS.getValue());
                    } catch (Exception e) {
                        deleteDir(new File(fileDir));
                        log.info("generatedData Exception {}", e.getMessage());
                        task.setFilePath(e.getMessage());
                        task.setStatus(ExportTaskStatusEnum.PROCESS_EXCEPTION.getValue());
                    }
                    task.setRunTime(new Date());
                    exportTaskDao.updateById(task);
                }
            }
        }
    }

    private String generatedData(Long startDate, Long endDate, List<ExportTaskVehicle> taskVehicleList, String fileDir,
                                 Long fwpTaskId, Integer dataType) {
        if (startDate == null || endDate == null || startDate > endDate) {
            return "";
        }
        log.info("generatedData start time : {}", DateFormatUtil.formatDate("yyyy-MM-dd HH:mm:ss.SSS", System.currentTimeMillis()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDate);
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH) + 1;
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(endDate);
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH) + 1;
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);

        String directory = properties.getForwardData().getDirectory();
        String compressFileName = properties.getForwardData().getCompressFileName();

        File compressFile = new File(directory);
        if (!compressFile.exists()) {
            compressFile.mkdirs();
        }

        Integer[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        List<String> fileInfoList = Lists.newArrayList();
        for (int year = startYear; year <= endYear; year++) {
            for (int month : months) {
                if ((year == startYear && month < startMonth)
                        || (year == endYear && month > endMonth)) {
                    continue;
                }

                int startIndex = 1;
                int dayCount = getDayOfMonth(year, month);
                if (year == startYear && month == startMonth) {
                    startIndex = startDay;
                }
                if (year == endYear && month == endMonth) {
                    dayCount = endDay;
                }

                for (int i = startIndex; i <= dayCount; i++) {
                    String strDay = leftAddOneZero(i);
                    String strMonth = leftAddOneZero(month);
                    String dir = fileDir + year + File.separator + strMonth + File.separator + strDay;
                    fileInfoList.add(dir);
                    dir += File.separator;
                    File file = new File(dir);
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    Long startTime = this.getStartTimeInMillis(year, month - 1, i);
                    Long endTime = startTime + (1000 * 60 * 60 * 24L);
                    if (startTime < startDate) {
                        startTime = startDate;
                    }
                    if (endTime > endDate) {
                        endTime = endDate;
                    }
                    log.debug("current export data startTime : {}, endTime {}, taskId {}, dataType {}", startTime, endTime, fwpTaskId, dataType);
                    for (ExportTaskVehicle taskVehicle : taskVehicleList) {
                        //查询数据为空不生成文件
                        log.debug("current export data vin {}, startTime : {}, endTime {}, taskId {}, dataType {}", taskVehicle.getVin(), startTime, endTime, fwpTaskId, dataType);
                        String fileName = dir + taskVehicle.getVin();
                        FileWriter writer = null;
                        try {
                            writer = new FileWriter(fileName, true);
                            this.writeDataToFile(writer, taskVehicle.getVin(), startTime, endTime, fwpTaskId, dataType);
                        } catch (IOException e) {
                            log.info("write file error !" + e.getMessage());
                        } finally {
                            if (writer != null) {
                                try {
                                    writer.close();
                                } catch (IOException e) {
                                    log.info(e.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        }

        for (String info : fileInfoList) {
            //压缩日期目录
            doCompress(info, info + COMPRESS_FILE_SUFFIX, false);
            //删除日期目录及文件
            deleteDir(new File(info));
        }

        String zipFileName = compressFileName + System.currentTimeMillis() + COMPRESS_FILE_SUFFIX;
        doCompress(fileDir, directory + zipFileName, true);

        //删除生成文件目录及文件
        deleteDir(new File(fileDir));

        log.info("generatedData end time : {}", DateFormatUtil.formatDate("yyyy-MM-dd HH:mm:ss.SSS", System.currentTimeMillis()));
        return directory + zipFileName;
    }


/**
     * 查询大数据接口获取正常转发报文数据
     *
     * @param writer    写文件流
     * @param vin       vin
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param fwpTaskId 转发任务id
     * @param dataType  数据类型（全部数据：0 实时转发：2 终端补发：3 平台补发：217）
     */

    private void writeDataToFile(FileWriter writer, String vin, Long startTime, Long endTime, Long fwpTaskId, Integer dataType) {
        String tableName = Global.fwpTableName(properties.getTstar().getTerminalType(), fwpTaskId);
        //如查询大数据接口获取次数过多影响性能，可以考虑延长查询时间减少创建查询器次数（现按天查询）或直接访问HBase获取数据
        platformService.queryRangeDataSuccess(fwpTaskId, vin, startTime, endTime, tableName, dataType,
                (RestDataRecord obj) -> {
                    try {
                        writer.write(DateFormatUtil.formatDate("yyyyMMddHHmmss", obj.getTime()));
                        writer.write(",");
                        writer.write(obj.getBody());
                        writer.write("\n");
                    } catch (IOException e) {
                        log.error("write file error!" + e.getMessage());
                    }
                });
    }

    private String leftAddOneZero(int s) {
        String str = String.valueOf(s);
        if (str.length() == 1) {
            str = "0" + str;
        }
        return str;
    }

    private Long getStartTimeInMillis(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }


/**
     * 获取当前年月天数，截至时间为系统时间：日
     *
     * @param year  年
     * @param month 月
     */

    private Integer getDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, 0);
        int dayCount = calendar.get(Calendar.DAY_OF_MONTH);
        if (year == nowYear && month == nowMonth && dayCount > nowDay) {
            dayCount = nowDay;
        }
        return dayCount;
    }


/**
     * 删除目录以及目录文件
     */

    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }


/**
     * 压缩文件
     *
     * @param directory   目录或者单个文件
     * @param zipName     压缩后的ZIP文件（目录+文件名称） D:/xx.zip
     * @param isRetainDir 是否保留目录压缩，如不保留目录压缩请确保各个目录文件名称不可重复，如重复压缩表只生成重复文件前文件
     */

    private void doCompress(String directory, String zipName, boolean isRetainDir) {
        ZipOutputStream out = null;
        try {
            File srcFile = new File(directory);
            File zipFile = new File(zipName);
            out = new ZipOutputStream(new FileOutputStream(zipFile));
            doCompress(srcFile, out, "", isRetainDir);
        } catch (IOException e) {
            log.info("doCompress exception:{}", e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            }

        }
    }

    private void doCompress(File inFile, ZipOutputStream out, String dir, boolean isRetainDir) {
        if (inFile.isDirectory()) {
            File[] files = inFile.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    String name = inFile.getName();
                    if (!"".equals(dir) && isRetainDir) {
                        name = dir + BACKSLASH + name;
                    }
                    doCompress(file, out, name, isRetainDir);
                }
            }
        } else {
            doZip(inFile, out, dir, isRetainDir);
        }
    }

    private void doZip(File inFile, ZipOutputStream out, String dir, boolean isDir) {
        FileInputStream fis = null;
        try {
            String entryName;
            if (!"".equals(dir) && isDir) {
                entryName = dir + BACKSLASH + inFile.getName();
            } else {
                entryName = inFile.getName();
            }
            ZipEntry entry = new ZipEntry(entryName);
            out.putNextEntry(entry);

            int len;
            byte[] buffer = new byte[1024];
            fis = new FileInputStream(inFile);
            while ((len = fis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
                out.flush();
            }
        } catch (IOException e) {
            log.info("doZip exception:{}", e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.closeEntry();
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        }
    }

    private String getIp() {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.info(e.getMessage());
        }
        byte[] ipArray = inetAddress.getAddress();
        String ipAddressStr = "";
        for (int i = 0; i < ipArray.length; i++) {
            if (i > 0) {
                ipAddressStr += ".";
            }
            ipAddressStr += ipArray[i] & 0xFF;
        }
        return ipAddressStr;
    }
}

