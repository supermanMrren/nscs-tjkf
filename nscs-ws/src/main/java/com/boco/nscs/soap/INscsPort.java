package com.boco.nscs.soap;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface INscsPort {
    //测试
    //String hello(@WebParam(name = "name") String name);

    //网络全专业工程信息查询
    String releaseEngineerInfo(@WebParam(name = "param")String param);
    //网络全专业故障信息查询
    String releaseFaultInfo(@WebParam(name = "param")String param);
    //2\3\4G覆盖信息接口
    String qryCoverInfo(@WebParam(name = "param")String param);
    //短信日志查询接口
    String qrySMSLog(@WebParam(name = "param")String param);
    //用户签约信息查询
    String qryHInfo(@WebParam(name = "param")String param);
    //HLR/HSS信息清除接口
    String clearHlrHssInfo(@WebParam(name = "param")String param);
    //打点接口
    String transferFaultInfo(@WebParam(name = "param")String param);
}
