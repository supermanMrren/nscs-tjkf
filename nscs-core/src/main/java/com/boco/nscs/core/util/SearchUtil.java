package com.boco.nscs.core.util;

import java.util.ArrayList;
import java.util.List;

import com.boco.nscs.core.AppProperties;
import com.boco.nscs.core.entity.*;
import com.github.pagehelper.PageInfo;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SearchUtil {
    @Autowired
    AppProperties appProp;

    private  static AppProperties appProperties;
    @PostConstruct
    public void init() {
        this.appProperties = appProp;
    }

    public static <T> PageResultBase parse(List<T> list){
        if (appProperties.getPageType().equalsIgnoreCase("datatables")){
            return parse4DataTables(list);
        }
        else  if (appProperties.getPageType().equalsIgnoreCase("easyui")){
            return parse4EasyUI(list);
        }
        else  if (appProperties.getPageType().equalsIgnoreCase("layui")){
            return parse4LayUI(list);
        }
        else {
            return parse4BizPageResult(list);
        }
    }

    public static <T> BizPageResult parse4BizPageResult(List<T> list){
        BizPageResult rtn = new BizPageResult();
        if(list!=null){
            PageInfo<T> page = new PageInfo<>(list);
            rtn.setTotal(page.getTotal());
            rtn.setPageIndex(page.getPageNum());
            rtn.setPageSize(page.getPageSize());
            rtn.setRows(list);
        }
        return rtn;
    }

    public static <T> EasyUIPageResult parse4EasyUI(List<T> list){
        EasyUIPageResult rtn = new EasyUIPageResult();
        if(list!=null){
            PageInfo<T> page = new PageInfo<>(list);
            rtn.setTotal(page.getTotal());
            rtn.setPage(page.getPageNum());
            rtn.setRecords(page.getPageSize());
            rtn.setRows(list);
        }
        return rtn;
    }

    public static <T> LayUIPageResult parse4LayUI(List<T> list){
        LayUIPageResult rtn = new LayUIPageResult();
        if(list!=null){
            PageInfo<T> page = new PageInfo<>(list);
            rtn.setCount(page.getTotal());
            rtn.setPage(page.getPageNum());
            rtn.setLimit(page.getPageSize());
            rtn.setCode(0);
            rtn.setData(list);
        }
        return rtn;
    }
    public static <T> DtPageResult parse4DataTables(List<T> list){
        DtPageResult rtn = new DtPageResult();
        if(list!=null){
            //用PageInfo对结果进行包装
            //page.getPageNum()  页数
            //page.getPageSize()
            // page.getTotal()
            PageInfo<T> page = new PageInfo<>(list);
            rtn.setiTotalRecords(page.getTotal());
            rtn.setiTotalDisplayRecords(page.getTotal());
            rtn.setAaData(list);
        }
        return rtn;
    }

    //easyui 树形结构转化为 带层级的下拉框
    public static List<SelectItem> EasyTree2TreeList(BizTree tree, String type){
        List<SelectItem> lst = new ArrayList<>();
        //根节点
        lst.add(new SelectItem(tree.getId(),tree.getName(),type));
        //递归添加
        lst.addAll(addTreeChild(tree.getChildren(),type,1));
        return  lst;
    }

    private static List<SelectItem> addTreeChild(List<BizTree> trees, String type, int level){
        List<SelectItem> lst = new ArrayList<>();

        if (trees== null)
            return lst;

        for (BizTree tree : trees) {
            //添加节点
            lst.add(new SelectItem(tree.getId(),"┣"+ StrUtil.repeat(" ━",level)+tree.getName(),type));
            //添加子节点
            lst.addAll(addTreeChild(tree.getChildren(),type,level+1));
        }

        return  lst;
    }
}