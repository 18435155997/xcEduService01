package com.xuecheng.manage_cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest   // 此注解，会找包manage_cms下面,springboot的启动类；然后扫描Spring包下面的bean，并将其加入容器；
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    // 以测试findAll方法为例
    // 从容器中，取我们刚刚编写好的dao接口 ；
    // 通过扫描dao接口，会生成这个接口的代理对象，然后我们就可以把这个代理对象注入到测试类当中；
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Test
    public void testFindAll(){
        List<CmsPage> list = cmsPageRepository.findAll();
        System.out.println(list);
    }

    // 自定义条件查询测试
    @Test
    public void testFindAllByExample() {
        // 分页参数定义
        int page = 0 ; // 当前页码，从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        // 参数1.条件值对象
        CmsPage cmsPage = new CmsPage();
        //cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");    // 站点Id
        //cmsPage.setTemplateId("5a925be7b00ffc4b3c1578b5");// 模板Id
        cmsPage.setPageAliase("轮播");                          // 别名
        // 参数2.条件匹配器ExampleMatcher
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher=exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        // ExampleMatcher.GenericPropertyMatchers.caseSensitive(); // 大小写
        // ExampleMatcher.GenericPropertyMatchers.contains();     // 模糊匹配
        // ExampleMatcher.GenericPropertyMatchers.endsWith();     // 以结束
        // ExampleMatcher.GenericPropertyMatchers.exact();        // 精确匹配
        // 定义Example
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher );
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println(all.getContent());
    }

    // 测试分页查询
    @Test
    public void testFindPage(){
        int page = 0 ; // 当前页码，从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> cms= cmsPageRepository.findAll(pageable);
        System.out.println(cms);
    }

    // 测试添加
    @Test
    public void testInsert(){
        // 定义实体类
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("01");
        cmsPage.setPageName("测试页面");

        List<CmsPageParam> cmsPageParams = new ArrayList<>();
        CmsPageParam cmsPageParam = new CmsPageParam();
        cmsPageParam.setPageParamName("param1");
        cmsPageParam.setPageParamValue("value1");
        cmsPageParams.add(cmsPageParam);
        cmsPage.setPageParams(cmsPageParams);
        cmsPage.setPageCreateTime(new Date());
        cmsPage.setTemplateId("t01");
        cmsPageRepository.save(cmsPage);
    }

    // 测试删除
    @Test
    public void testDelete(){
        cmsPageRepository.deleteById("5dd23a0fc053123cb0c745c9");
    }

    // 测试修改
    @Test
    public void testUpdate(){
        Optional<CmsPage> optional = cmsPageRepository.findById("5dd23b11c053121988d4434c");// alt+shift+L
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("测试页面01");
            cmsPageRepository.save(cmsPage);
        }
    }
}
