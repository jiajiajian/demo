package cn.com.tiza.web.rest;

import cn.com.tiza.dao.VehicleDao;
import cn.com.tiza.domain.Vehicle;
import cn.com.tiza.excel.read.ExcelReader;
import cn.com.tiza.rest.ExcelController;
import cn.com.tiza.service.RedisQueryService;
import cn.com.tiza.service.VehicleDebugService;
import cn.com.tiza.service.VehicleService;
import cn.com.tiza.service.dto.*;
import cn.com.tiza.service.mapper.VehicleMapper;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Slf4j
@RestController
@RequestMapping("/vehicle")
public class VehicleController extends ExcelController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private VehicleDao vehicleDao;

    @Autowired
    private FileApiClient fileApiClient;

    @Autowired
    private VehicleDebugService vehicleDebugService;

    @Autowired
    private AccountApiClient accountApiClient;

    @Autowired
    private RedisQueryService redisQueryService;

    @GetMapping
    public ResponseEntity<List<VehicleVM>> list(VehicleQuery query) {

        PageQuery pageQuery = vehicleService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @PostMapping("/batchQuery")
    public ResponseEntity<List<VehicleVM>> batchQuery(@RequestParam("file") MultipartFile file) {
        ExcelReader reader = ExcelReader
                .createInstance(2, VehicleBatchQueryImportDto::new, null, VehicleBatchQueryImportDto.ruleValidator());
        List<VehicleVM> vehicleVMList = new ArrayList<>();
        try {
            List<VehicleBatchQueryImportDto> list = reader.create(file.getInputStream()).resolve();
            HashSet<String> vinSet = Sets.newHashSetWithExpectedSize(list.size());
            //校验文件vin是否重复
            list.forEach(dto -> reader.checkDuplicate(vinSet, dto.getVin(), dto, VehicleServiceDateDto.IDX_VIN, "VIN不可重复"));
            //检查vin在表中是否存在
            vehicleService.checkBatchQueryVinList(reader, list);
            if (reader.hasError()) {
                String fileId = fileApiClient.save(FileDto.buildTempFile(file.getOriginalFilename(),
                        reader.writeErrorFileToBytes()));
                throw new BadRequestException(ErrorConstants.IMPORT_EXCEL_FILE_ERROR, fileId);
            }
            List<String> vinList = list.stream().map(VehicleBatchQueryImportDto::getVin).collect(Collectors.toList());
            //查询
            vehicleVMList = vehicleService.batchSearch(vinList);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return ResponseEntity.ok(vehicleVMList);
    }

    @GetMapping("{id}")
    public ResponseEntity<VehicleVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(vehicleService.get(id)
                .map(obj -> vehicleMapper.toVM(obj)));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody VehicleDto dto) {
        vehicleService.create(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid VehicleDto dto) {
        Optional<Vehicle> updatedObj = vehicleService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleDebugService.deleteByVehicleId(id);
        vehicleService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        vehicleDebugService.deleteByVehicleId(ids);
        vehicleService.delete(ids);
        return ResponseEntity.ok().build();
    }

    /**
     * 销售信息编辑
     *
     * @param id
     * @param saleDto
     */

    @PutMapping("/updateSaleInfo/{id}")
    public void updateSaleInfo(@PathVariable Long id, @RequestBody VehicleSaleDto saleDto) {
        vehicleService.updateSaleInfo(id, saleDto);
    }

    /**
     * 获取服务状态
     */
    @GetMapping("/getServiceInfo/{id}")
    public ResponseEntity<VehicleServiceInfoVM> getServiceInfo(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(vehicleService.get(id)
                .map(obj -> vehicleMapper.toServiceInfoVM(obj)));
    }


    /**
     * 修改服务状态
     *
     * @param id
     * @param serviceDto
     */
    @PutMapping("/updateServiceInfo/{id}")
    public void updateServiceInfo(@PathVariable Long id, @RequestBody VehicleServiceDto serviceDto) {
        vehicleService.updateServiceInfo(id, serviceDto);
    }

    /**
     * 获取调试日期
     *
     * @param vin
     * @return
     */
    @GetMapping("/debugDate/{vin}")
    public ResponseEntity<DebugDateVM> debugDate(@PathVariable String vin) {
        return ResponseEntity.ok(vehicleService.getDebugDate(vin));
    }

    /**
     * 修改调试时间
     *
     * @param vin
     * @param endTime
     */
    @PutMapping("/updateDebugDate/{vin}/{endTime}")
    public void updateDebugDate(@PathVariable("vin") String vin,
                                @PathVariable("endTime") Long endTime) {
        vehicleService.updateDebugDate(vin, endTime);
    }

    /**
     * 根据vin查找车辆终端相关信息信息
     *
     * @param vin
     * @return
     */
    @GetMapping("/findTerminalInfoByVin/{vin}")
    public ResponseEntity<TerminalInfoVM> findTerminalInfoByVin(@PathVariable String vin) {
        return ResponseEntity.ok(vehicleService.findTerminalInfoByVin(vin));
    }

    /**
     * 根据终端编号查询终端信息
     *
     * @param terminalCode
     * @return
     */

    @GetMapping("/terminalInfo/{terminalCode}")
    public ResponseEntity<TerminalInfoVM> changeTerminal(@PathVariable("terminalCode") String terminalCode) {
        return ResponseEntity.ok(Optional.ofNullable(vehicleDao.findTerminalByCode(terminalCode)).orElseGet(TerminalInfoVM::new));
    }

    /**
     * 更换终端
     *
     * @param vin
     * @param newTerminalCode
     * @return
     */
    @PutMapping("/changeTerminal/{vin}/{newTerminalCode}")
    public ResponseEntity<Void> changeTerminal(@PathVariable("vin") String vin,
                                               @PathVariable("newTerminalCode") String newTerminalCode) {
        vehicleService.changeTerminal(vin, newTerminalCode);
        return ResponseEntity.ok().build();
    }

    /**
     * 双车互换终端
     *
     * @param vin1
     * @param vin2
     * @return
     */
    @PutMapping("/swapTerminal/{vin1}/{vin2}")
    public ResponseEntity<Void> swapTerminal(@PathVariable("vin1") String vin1,
                                             @PathVariable("vin2") String vin2) {
        vehicleService.swapTerminal(vin1, vin2);
        return ResponseEntity.ok().build();
    }

    /**
     * 移机
     *
     * @param oldVin
     * @param newVin
     * @return
     */
    @PutMapping("/moveVin/{oldVin}/{newVin}")
    public void moveVin(@PathVariable("oldVin") String oldVin,
                        @PathVariable("newVin") String newVin) {
        vehicleService.moveVin(oldVin, newVin);
    }

    /**
     * 延长车辆服务期
     *
     * @param file
     */
    @PostMapping("/updateServiceDate")
    public void updateServiceDate(@RequestParam("file") MultipartFile file) {
        ExcelReader reader = ExcelReader
                .createInstance(2, VehicleServiceDateDto::new, null, VehicleServiceDateDto.ruleValidator());
        try {
            List<VehicleServiceDateDto> list = reader.create(file.getInputStream()).resolve();
            HashSet<String> vinSet = Sets.newHashSetWithExpectedSize(list.size());
            //校验文件vin是否重复
            list.forEach(dto -> reader.checkDuplicate(vinSet, dto.getVin(), dto, VehicleServiceDateDto.IDX_VIN, "VIN不可重复"));
            //校验vin是否存在
            List<String> vinList = list.stream()
                    .map(VehicleServiceDateDto::getVin)
                    .collect(Collectors.toList());
            List<Vehicle> vehicleList = vehicleDao.vehicleListByVinList(vinList);
            vehicleService.checkUpdateServiceDateVin(reader, list, vehicleList);

            if (reader.hasError()) {
                String fileId = fileApiClient.save(FileDto.buildTempFile(file.getOriginalFilename(),
                        reader.writeErrorFileToBytes()));
                throw new BadRequestException(ErrorConstants.IMPORT_EXCEL_FILE_ERROR, fileId);
            }
            //更新服务期
            vehicleService.updateServiceDate(list, vehicleList);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    /**
     * 车辆服务暂停
     *
     * @param file
     */
    @PostMapping("/suspendedVehicleService")
    public void suspendedVehicleService(@RequestParam("file") MultipartFile file) {
        ExcelReader reader = ExcelReader
                .createInstance(2, VehicleServiceSuspendedImportDto::new, null, VehicleServiceSuspendedImportDto.ruleValidator());
        try {
            List<Vehicle> vehicleList = checkSuspendedImportExcel(file, reader, false);
            //暂停服务
            if (vehicleList.isEmpty()) {
                return;
            }
            vehicleService.suspendedServiceStatus(vehicleList);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    /**
     * 校验服务暂停/恢复 文件内容
     *
     * @param file
     * @param reader
     * @param isRestore true:暂停恢复 false:暂停
     * @return
     * @throws IOException
     */
    private List<Vehicle> checkSuspendedImportExcel(@RequestParam("file") MultipartFile file, ExcelReader reader, boolean isRestore) throws IOException {
        List<VehicleServiceSuspendedImportDto> list = reader.create(file.getInputStream()).resolve();
        if (list.isEmpty()) {
            return Lists.newArrayList();
        }
        HashSet<String> vinSet = Sets.newHashSetWithExpectedSize(list.size());
        //校验文件vin是否重复
        list.forEach(dto -> reader.checkDuplicate(vinSet, dto.getVin(), dto, VehicleServiceDateDto.IDX_VIN, "VIN不可重复"));
        //校验vin是否存在和 服务状态
        List<String> vinList = list.stream()
                .map(VehicleServiceSuspendedImportDto::getVin)
                .collect(Collectors.toList());
        List<Vehicle> vehicleList = vehicleDao.vehicleListByVinList(vinList);
        vehicleService.checkSuspendedVin(reader, list, vehicleList, isRestore);
        if (reader.hasError()) {
            String fileId = fileApiClient.save(FileDto.buildTempFile(file.getOriginalFilename(),
                    reader.writeErrorFileToBytes()));
            throw new BadRequestException(ErrorConstants.IMPORT_EXCEL_FILE_ERROR, fileId);
        }
        return vehicleList;
    }

    /**
     * 车辆服务暂停恢复
     *
     * @param file
     */
    @PostMapping("/vehicleServiceRestore")
    public void vehicleServiceRestore(@RequestParam("file") MultipartFile file) {
        ExcelReader reader = ExcelReader
                .createInstance(2, VehicleServiceSuspendedImportDto::new, null, VehicleServiceSuspendedImportDto.ruleValidator());
        try {
            List<Vehicle> vehicleList = checkSuspendedImportExcel(file, reader, true);
            if (vehicleList.isEmpty()) {
                return;
            }
            //暂停恢复
            vehicleService.restoreServiceStatus(vehicleList);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }


    /**
     * 销户确认是否存在服务已开通的车辆
     */
    @PostMapping("/closeAccountConfirm")
    public ResponseEntity<List<String>> closeAccountConfirm(@RequestParam("file") MultipartFile file) {
        List<String> result = Lists.newArrayList();
        ExcelReader reader = ExcelReader
                .createInstance(2, VehicleServiceSuspendedImportDto::new, null, VehicleServiceSuspendedImportDto.ruleValidator());
        try {
            List<Vehicle> vehicleList = checkCloseExcelImportExcel(file, reader);
            //校验是否存在服务已开通车辆
            if (!vehicleList.isEmpty()) {
                result = vehicleService.findVehicleServiceIsOpened(vehicleList);
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return ResponseEntity.ok(result);
    }


    /**
     * 车辆销户
     */
    @PostMapping("/closeAccount")
    public void closeAccount(@RequestParam("file") MultipartFile file) {
        ExcelReader reader = ExcelReader
                .createInstance(2, VehicleServiceSuspendedImportDto::new, null, VehicleServiceSuspendedImportDto.ruleValidator());
        try {
            List<Vehicle> vehicleList = checkCloseExcelImportExcel(file, reader);
            //删除车辆对应的终端信息，并且终端绑定的SIM卡自动返回到SIM卡池中，SIM卡变成预销户状态，预销户状态
            if (!vehicleList.isEmpty()) {
                vehicleService.closeAccount(vehicleList);
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    /**
     * 校验销户文件
     *
     * @param file
     * @param reader
     * @return
     * @throws IOException
     */
    private List<Vehicle> checkCloseExcelImportExcel(MultipartFile file, ExcelReader reader) throws IOException {
        List<VehicleServiceSuspendedImportDto> list = reader.create(file.getInputStream()).resolve();
        if (list.isEmpty()) {
            return Lists.newArrayList();
        }
        HashSet<String> vinSet = Sets.newHashSetWithExpectedSize(list.size());
        //校验文件vin是否重复
        list.forEach(dto -> reader.checkDuplicate(vinSet, dto.getVin(), dto, VehicleServiceDateDto.IDX_VIN, "VIN不可重复"));
        //校验vin是否存在
        List<String> vinList = list.stream()
                .map(VehicleServiceSuspendedImportDto::getVin)
                .collect(Collectors.toList());
        List<Vehicle> vehicleList = vehicleDao.vehicleListByVinList(vinList);
        vehicleService.checkCloseAccountVin(reader, list);
        if (reader.hasError()) {
            String fileId = fileApiClient.save(FileDto.buildTempFile(file.getOriginalFilename(),
                    reader.writeErrorFileToBytes()));
            throw new BadRequestException(ErrorConstants.IMPORT_EXCEL_FILE_ERROR, fileId);
        }
        return vehicleList;
    }


    /**
     * 导入车辆
     *
     * @param file
     */
    @PostMapping("/updateVehicle")
    public void updateVehicle(@RequestParam("file") MultipartFile file) {
        ExcelReader reader = ExcelReader
                .createInstance(2, VehicleImportDto::new, null, VehicleImportDto.ruleValidator());
        try {
            List<VehicleImportDto> list = reader.create(file.getInputStream()).resolve();
            if (list.isEmpty()) {
                return;
            }
            HashSet<String> vinSet = Sets.newHashSetWithExpectedSize(list.size());
            HashSet<String> terminalSet = Sets.newHashSetWithExpectedSize(list.size());
            HashSet<String> simcardSet = Sets.newHashSetWithExpectedSize(list.size());
            //校验文件vin是否重复
            list.forEach(dto -> reader.checkDuplicate(vinSet, dto.getVin(), dto, VehicleImportDto.IDX_VIN, "VIN不可重复"));
            list.forEach(dto -> reader.checkDuplicate(terminalSet, dto.getVin(), dto, VehicleImportDto.IDX_TERMINAL_CODE, "终端编号不可重复"));
            list.forEach(dto -> reader.checkDuplicate(simcardSet, dto.getVin(), dto, VehicleImportDto.IDX_SIM_CARD, "SIM卡号不可重复"));
            //校验数据权限
            vehicleService.checkVehicleImport(reader, list);

            if (reader.hasError()) {
                String fileId = fileApiClient.save(FileDto.buildTempFile(file.getOriginalFilename(),
                        reader.writeErrorFileToBytes()));
                throw new BadRequestException(ErrorConstants.IMPORT_EXCEL_FILE_ERROR, fileId);
            }
            //校验车辆类型和车型
            vehicleService.checkImportModel(reader, list);
            if (reader.hasError()) {
                String fileId = fileApiClient.save(FileDto.buildTempFile(file.getOriginalFilename(),
                        reader.writeErrorFileToBytes()));
                throw new BadRequestException(ErrorConstants.IMPORT_EXCEL_FILE_ERROR, fileId);
            }
            //更新
            vehicleService.updateVehicle(list);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }


    @GetMapping("/export")
    public void export(VehicleQuery query, HttpServletRequest request, HttpServletResponse response) {
        String[] titles = {"机器序列号", "终端编号", "SIM卡号", "所属机构", "融资机构", "车辆类型", "车辆型号", "累计工作小数数（H）",
                "ACC状态", "定位状态", "最新时间", "定位有效时间", "GPS位置", "锁车状态", "服务状态", "服务开始", "服务结束", "服务期限（月）", "销售状态", "合同号",
                "客户名称", "客户电话", "报警联系人", "24H联系电话", "调试状态", "调试开始", "调试结束", "注册时间"};
        query.setPage(1);
        query.setLimit(Integer.MAX_VALUE);
        int[] permissions = {109611};
        boolean b = accountApiClient.checkPermission(permissions);
        query.setLocation(b);
        if (!b) {
            ArrayList<String> titleList = new ArrayList<>(Arrays.asList(titles));
            titleList.remove(12);
            titles = titleList.toArray(new String[titleList.size()]);
        }

        List<VehicleVM> list = vehicleService.findAll(query).getList();
        if (b) {
            download("车辆", titles, list, VehicleVM::toRow, request, response);
        } else {
            download("车辆", titles, list, VehicleVM::toRow1, request, response);
        }

    }

    @GetMapping("/baseInfoByVin/{vin}")
    public ResponseEntity<VehicleBaseInfoVM> baseInfoByVin(@PathVariable String vin) {
        return ResponseEntity.ok(vehicleService.baseInfoByVin(vin));
    }

    @GetMapping("/vehicleServiceExpireJob")
    public void vehicleServiceStatusJob() {
        vehicleService.vehicleServiceStatusJob();
    }

    @GetMapping("/rootOrgByVin/{vin}")
    public Long rootOrgByVin(@PathVariable String vin) {
        return vehicleDao.rootOrgIdByVin(vin);
    }

    /**
     * 获取车辆实时工况数据
     */
    @GetMapping("/ciData/{vin}")
    public Map<String, String> ciData(@PathVariable String vin) {
        return redisQueryService.ciData(vin);
    }
}
