package com.wwp.common.util;



import com.wwp.entity.DepartIdModel;
import com.wwp.entity.SysDepart;
import com.wwp.entity.SysDepartTreeModel;
import com.wwp.entity.SysUser;

import java.util.ArrayList;
import java.util.List;

/**
 * <P>
 * 对应部门的表,处理并查找树级数据
 * <P>
 * 
 * @Author: Steve
 * @Date: 2019-01-22
 */
public class FindsDepartsChildrenUtil {

	//部门树信息-树结构
	//private static List<SysDepartTreeModel> sysDepartTreeList = new ArrayList<SysDepartTreeModel>();
	
	//部门树id-树结构
    //private static List<DepartIdModel> idList = new ArrayList<>();


    /**
     * queryTreeList的子方法 ====1=====
     * 这里返回的是 parentCategory 及其子子孙孙所有节点，如果parentCategory =1 那就是全部节点
     * 但都是从recordList  里面筛选出来的
     */
    public static List<SysDepartTreeModel> wrapTreeDataToTreeList(List<SysDepart> recordList, Integer parentCategory) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
    	List<DepartIdModel> idList = new ArrayList<>();
        List<SysDepartTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepart depart = recordList.get(i);
            records.add(new SysDepartTreeModel(depart));
        }
        List<SysDepartTreeModel> tree = findChildren(records, idList, parentCategory);

       // setEmptyChildrenAsNull(tree);
        return tree;
    }

    /**
     * 获取 DepartIdModel
     * @param recordList
     * @return 这里返回的是 parentCategory 及其子子孙孙所有节点，如果parentCategory =1 那就是全部节点
     * 但都是从recordList  里面筛选出来的
     */
    public static List<DepartIdModel> wrapTreeDataToDepartIdTreeList(List<SysDepart> recordList, int parentCategory) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
        List<DepartIdModel> idList = new ArrayList<>();
        List<SysDepartTreeModel> records = new ArrayList<>();

        for (int i = 0; i < recordList.size(); i++) {
            SysDepart depart = recordList.get(i);
            records.add(new SysDepartTreeModel(depart));
        }
        findChildren(records, idList, parentCategory);
        setEmptyChildrenAsNull(idList);

        return idList;
    }

    /**
     * queryTreeList的子方法 ====2=====
     * 该方法是找到并封装顶级父类的节点到TreeList集合
     */
    private static List<SysDepartTreeModel> findChildren(List<SysDepartTreeModel> recordList,
             List<DepartIdModel> departIdList, Integer parentCategory) {

        List<SysDepartTreeModel> treeList = new ArrayList<>();

        for (int i = 0; i < recordList.size(); i++) {
            SysDepartTreeModel branch = recordList.get(i);
            if( branch.getOrgCategory().equals(parentCategory)){//只找下一级儿子 parentCategory 是1 后就是顶层集团
                treeList.add(branch);
                DepartIdModel departIdModel = new DepartIdModel().convert(branch);
                departIdList.add(departIdModel);
            }
        }

        getGrandChildren(treeList,recordList,departIdList);

        return treeList;
    }

    /**
     * queryTreeList的子方法====3====
     *该方法是找到父类下的所有子节点集合并封装到TreeList集合
     */
    private static void getGrandChildren(List<SysDepartTreeModel> treeList,List<SysDepartTreeModel> recordList,List<DepartIdModel> idList) {

        for (int i = 0; i < treeList.size(); i++) {
            SysDepartTreeModel model = treeList.get(i);
            DepartIdModel idModel = idList.get(i);
            for (int i1 = 0; i1 < recordList.size(); i1++) {
                SysDepartTreeModel m = recordList.get(i1);
                if (m.getParentId()!=null && m.getParentId().equals(model.getId())) {
                    model.getChildren().add(m);
                    DepartIdModel dim = new DepartIdModel().convert(m);
                    idModel.getChildren().add(dim);
                }
            }
            getGrandChildren(treeList.get(i).getChildren(), recordList, idList.get(i).getChildren());
        }

    }
    

    /**
     * queryTreeList的子方法 ====4====
     * 该方法是将子节点为空的List集合设置为Null值
     */
    private static void setEmptyChildrenAsNull(List<DepartIdModel> treeList) {

        for (int i = 0; i < treeList.size(); i++) {
            DepartIdModel model = treeList.get(i);
            if (model.getChildren().size() == 0) {
                model.setChildren(null);
            }else{
                setEmptyChildrenAsNull(model.getChildren());
            }
        }
        // sysDepartTreeList = treeList;
    }

}
