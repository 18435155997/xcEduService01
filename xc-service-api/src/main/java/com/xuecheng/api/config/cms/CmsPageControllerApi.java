package com.xuecheng.api.config.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="cms页面管理接口",description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {

    // 页面查询 - 分页参数（当前页码 page； 每页显示记录数 size） & 页面查询条件
    // 规定：本项目中，所有查询列表的响应结果，均采用QueryResponseResult
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "页 码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录 数",required=true,paramType="path",dataType="int") })
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    // 新增接口 - 新增接口参数为CmsPage界面录入的信息；接口返回值为CmsPage以及请求结果
    @ApiOperation("添加页面")
    public CmsPageResult add(CmsPage cmsPage);

    // 通过Id查询页面
    @ApiOperation("通过Id查询页面")
    public CmsPage findById(String id);

    // 修改页面
    @ApiOperation("修改页面")
    public CmsPageResult update(String id,CmsPage cmsPage);

    // 删除页面
    @ApiOperation("删除页面")
    public ResponseResult delete(String id);
}
