package cn.com.tiza.cmd.kafka.entity;


import cn.com.tiza.cmd.kafka.util.DataConverter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


public class TStarData {
    private static final byte DATA_VERSION_1 = 1;
    private final byte dataVersion;
    private String terminalID;
    private long time;
    private String cmdID;
    private int cmdSerialNo;
    private byte[] msgBody;

    public TStarData() {
        this.dataVersion = 1;
    }


    public TStarData(byte dataVersion) {
        this.dataVersion = dataVersion;
    }


    public TStarData(String terminalID, long time, String cmdID, int cmdSerialNo, byte[] msgBody) {
        this.dataVersion = 1;
        this.terminalID = terminalID;
        this.time = time;
        this.cmdID = cmdID;
        this.cmdSerialNo = cmdSerialNo;
        this.msgBody = msgBody;
    }

    public TStarData(byte dataVersion, String terminalID, long time, String cmdID, int cmdSerialNo, byte[] msgBody) {
        this.dataVersion = dataVersion;
        this.terminalID = terminalID;
        this.time = time;
        this.cmdID = cmdID;
        this.cmdSerialNo = cmdSerialNo;
        this.msgBody = msgBody;
    }


    public String getTerminalID() {
        return this.terminalID;
    }


    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }


    public long getTime() {
        return this.time;
    }


    public void setTime(long time) {
        this.time = time;
    }


    public String getCmdID() {
        return this.cmdID;
    }


    public void setCmdID(String cmdID) {
        this.cmdID = cmdID;
    }


    public int getCmdSerialNo() {
        return this.cmdSerialNo;
    }


    public void setCmdSerialNo(int cmdSerialNo) {
        this.cmdSerialNo = cmdSerialNo;
    }


    public byte[] getMsgBody() {
        return this.msgBody;
    }


    public void setMsgBody(byte[] msgBody) {
        this.msgBody = msgBody;
    }


    public byte getDataVersion() {
        return this.dataVersion;
    }


    public byte[] toBytes() {
        int leastLen = 19;
        byte[] terminalIDbytes = getTerminalID().getBytes(StandardCharsets.UTF_8);
        byte[] cmdIdBytes = getCmdID().getBytes(StandardCharsets.UTF_8);
        int bufLen = leastLen + terminalIDbytes.length + cmdIdBytes.length + (getMsgBody()).length;
        ByteBuffer dataBuf = ByteBuffer.allocate(bufLen);
        dataBuf.put(getDataVersion());
        dataBuf.put((byte) terminalIDbytes.length);
        dataBuf.put(terminalIDbytes);
        dataBuf.putLong(getTime());
        dataBuf.put((byte) cmdIdBytes.length);
        dataBuf.put(cmdIdBytes);
        dataBuf.putInt(getCmdSerialNo());
        dataBuf.putInt((getMsgBody()).length);
        dataBuf.put(getMsgBody());
        return dataBuf.array();
    }

    @Override
    public String toString() {
        return "TStarData{dataVersion=" + this.dataVersion + ", terminalID='" + this.terminalID + '\'' + ", time=" + this.time + ", cmdID='" + this.cmdID + '\'' + ", cmdSerialNo=" + this.cmdSerialNo + ", msgBody=" +
                DataConverter.bytes2Hex(this.msgBody) + '}';
    }


    public static TStarData bytes2TStarData(byte[] bytes, int offset, int length) {
        if (bytes == null) {
            throw new IllegalArgumentException("Deserialize TStarData error, bytes cannot be null.");
        }
        if (length < 19) {
            throw new IllegalArgumentException("Deserialize TStarData error, bytes length cannot be less than 19.");
        }
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        byte dataVersion = buffer.get();
        TStarData tstarData = new TStarData(dataVersion);

        byte terminalIDBytesLength = buffer.get();
        byte[] terminalIDBytes = new byte[terminalIDBytesLength];
        buffer.get(terminalIDBytes);
        String terminalID = new String(terminalIDBytes, StandardCharsets.UTF_8);
        tstarData.setTerminalID(terminalID);

        long time = buffer.getLong();
        tstarData.setTime(time);

        byte cmdIDBytesLength = buffer.get();
        byte[] cmdBytes = new byte[cmdIDBytesLength];
        buffer.get(cmdBytes);
        String cmdID = new String(cmdBytes, StandardCharsets.UTF_8);
        tstarData.setCmdID(cmdID);

        int cmdSerialNo = buffer.getInt();
        tstarData.setCmdSerialNo(cmdSerialNo);

        int msgBodyLength = buffer.getInt();
        byte[] msgBody = new byte[msgBodyLength];
        buffer.get(msgBody);
        tstarData.setMsgBody(msgBody);

        return tstarData;
    }
}
