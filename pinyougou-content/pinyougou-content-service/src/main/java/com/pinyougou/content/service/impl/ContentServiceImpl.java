package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.mapper.ContentMapper;
import com.pinyougou.pojo.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service(interfaceName = "com.pinyougou.content.service.ContentService")
@Transactional
public class ContentServiceImpl implements ContentService {
    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult findByPage(Integer page, Integer rows) {
        try {
            PageInfo<Object> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    contentMapper.selectAll();
                }
            });
            PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());
            return pageResult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveContent(Content content) {
        try {
            contentMapper.insertSelective(content);
            try {
                /** 清除Redis缓存 */
                redisTemplate.delete("content");
            } catch (Exception ex) {
                System.out.println("清除缓存失败");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateContent(Content content) {
        try {
            contentMapper.updateByPrimaryKeySelective(content);
            try {
                /** 清除Redis缓存 */
                redisTemplate.delete("content");
            } catch (Exception ex) {
                System.out.println("清除缓存失败");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteContent(Long[] ids) {
        try {
            for (Long id : ids) {
                contentMapper.deleteByPrimaryKey(id);
            }
            try {
                /** 清除Redis缓存 */
                redisTemplate.delete("content");
            } catch (Exception ex) {
                System.out.println("清除缓存失败");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Content> findContentByCategoryById(Long id) {
        try {
            List<Content> contents = null;
            //从缓存获取
            try {
                System.out.println("从缓存中取广告信息");
                contents = (List<Content>) redisTemplate.boundValueOps("content").get();
                if (contents != null && contents.size() > 0) {
                    return contents;
                }
            } catch (Exception ex) {
                System.out.println("从缓存中取首页轮播失败");
                //ex.printStackTrace();
            }
            //从mysql中获取
            Example example = new Example(Content.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("categoryId", id);
            criteria.andEqualTo("status", 1);
            example.orderBy("sortOrder").desc();
            contents = contentMapper.selectByExample(example);
            //放入缓存中
            try {
                System.out.println("把广告信息放入缓存");
                redisTemplate.boundValueOps("content").set(contents);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            return contents;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
