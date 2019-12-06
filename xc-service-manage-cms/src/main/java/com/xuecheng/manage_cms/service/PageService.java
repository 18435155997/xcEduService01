package com.xuecheng.manage_cms.service;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;

    /**
     *
     * @param page 页码：从1开始计数
     * @param size 每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page , int size, QueryPageRequest queryPageRequest) {
        if(queryPageRequest == null){
            queryPageRequest = new QueryPageRequest();
        }
        // 参数1.条件值对象
        CmsPage cmsPage = new CmsPage();
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        // 参数2.条件匹配器ExampleMatcher
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher );
        if(page<=0){
            page =1;
        }
        page = page-1;
        if(size<=0){
            size = 10;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> cms = cmsPageRepository.findAll(example, pageable);
        // Page<CmsPage> cms= cmsPageRepository.findAll(pageable);
        QueryResult<CmsPage> queryResult = new QueryResult();
        queryResult.setList(cms.getContent());         // 数据列表
        queryResult.setTotal(cms.getTotalElements());  // 数据总记录数

        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    /**
     * 实现调用dao，新增页面
     * @param cmsPage
     * @return
     */
    public CmsPageResult add(CmsPage cmsPage){
        // 校验页面名称、站点Id、页面webpath的唯一性
        // 根据页面名称、站点Id、页面webpath三个字段，查询CmsPage集合；如果查询到，说明此页面信息已经存在；如果查询不到，再继续新增
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(cmsPage1 == null){
            cmsPage.setPageId(null);            // 此处无论入参cmsPage中的PageId是否为空，都将PageId设置为空，保证由mondodb统一生成主键
            cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        }
        // 如果不走上面的if，弹出提示“添加失败”
        return new CmsPageResult(CommonCode.FAIL,null);
        // 调用Dao新增页面
    }

    /**
     * 根据Id查询页面
     * @param id
     * @return
     */
    public CmsPage findById(String id){
        Optional<CmsPage> byId = cmsPageRepository.findById(id);
        if(byId.isPresent()){
            return byId.get();
        }
        return null;
    }

    /**
     * 更新页面信息
     */
    public CmsPageResult update(String id, CmsPage cmsPage){
        CmsPage one = findById(id);
        if(one!=null){
            //更新模板Id
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());

            CmsPage save = cmsPageRepository.save(one);
            if(save!=null){
                return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
            }
        }
        // 如果不走上面的if，弹出提示“添加失败”
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    /**
     * 根据pageId删除页面信息
     */
    public ResponseResult delete(String id){
        // 先查询，再删除
        Optional<CmsPage> one = cmsPageRepository.findById(id);
        if(one.isPresent()){
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
}
