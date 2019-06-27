package com.boco.nscs.common;


import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlHandler {

    private  static Logger logger = LoggerFactory.getLogger(XmlHandler.class);

    public static List<Map<String,String>> getSoapResponse(Document doc){
        Element root = doc.getRootElement();
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        //调用getChildAllText方法。获取目标子节点的值 <
        List<Map<String,String>> reultList = getAllChild(doc, root,list);
        return reultList;
    }

    public static List<Map<String,String>> getAllChild(Document doc, Element e,List<Map<String,String>> reultList) {
        if (e != null) {
            if (e.elements() != null)   //如果存在子节点
            {
                List<Element> list = e.elements();
                for (Element el : list){    //循环输出
                    if("KvPair".equals(el.getName())) {
                        Map<String,String> kpMap = new HashMap<String, String>();
                        List<Element> elements = el.elements();
                        for(Element e2 :elements){
                            if ("ENUMReportText".equals(e2.getName())){
                                getAllChild(doc, e2, reultList);
                            }
                            kpMap.put(e2.getName(), e2.getTextTrim());//将叶子节点值压入map
                        }
                        reultList.add(kpMap);
                    }
                    if("SmsLog".equals(el.getName())) {
                        Map<String,String> kpMap = new HashMap<String, String>();
                        List<Element> elements = el.elements();
                        for(Element e2 :elements){
                            kpMap.put(e2.getName(), e2.getTextTrim());//将叶子节点值压入map
                        }
                        reultList.add(kpMap);
                    }
                    if (el.elements().size() > 0)   //如果子节点还存在子节点，则递归获取
                    {
                        getAllChild(doc, el, reultList);
                    }
                }
            }
        }
        return reultList;
    }
}
