
package com.blog.service.IMP;


import com.blog.model.Blog;
import com.blog.model.Type;
import com.blog.repository.IBlogRepository;
import com.blog.service.IBlogService;
import com.blog.util.MarkdownUtils;
import com.blog.vo.BlogQuery;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogService implements IBlogService {


    @Autowired
    IBlogRepository iBlogRepository;


    @Override
    @Transactional
    public Blog getBlog(Long id) {
        return iBlogRepository.getOne(id);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) throws NotFoundException {

        Optional<Blog> blog = Optional.of(iBlogRepository.getOne(id));

        if(!blog.isPresent()){
            throw new NotFoundException("該BLog不存在");
        }
        Blog blogGetById = blog.get();

        Blog blogTarget = new Blog();

        BeanUtils.copyProperties(blogGetById,blogTarget);

        String content = blogTarget.getContent();

        blogTarget.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        //更新瀏覽次數
        iBlogRepository.updateViews(id);

        return blogTarget;
    }


    //有對象的查詢
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        //findAll 選 Specification 那一個
        return iBlogRepository.findAll(new Specification<Blog>() {
            //root 是查詢對象 , criteriaQuery 查詢容器 , criteriaBuilder條件表達式
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<>();

                //找Title
                //如果title不為空值
                if (!"".equals(blog.getTitle()) && blog.getTitle() !=null){
                    //取名為SQL為title的 (屬性名字,屬性值)
                    predicates.add(cb.like(root.<String>get("title") ,"%"+blog.getTitle()+"%"));
                }
                //找分類
                if (blog.getTypeId() != null){
                    //拿到Type類型 type表裡面的ID
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                //是否推薦
                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend") , blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));

                return null;
            }
        } , pageable);

    }

    //沒使用查詢功能的
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return iBlogRepository.findAll(pageable);
    }

    //tag used
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {

        return iBlogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                //Join 關聯查詢
                Join join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return iBlogRepository.findByQuery(query, pageable);
    }



    @Override
    public List<Blog> listRecommendBlog(Integer size) {

        //https://blog.csdn.net/weixin_44216706/article/details/106480251
        //查看源码，原来是Sort的构造器私有了private；所以不能通过new Sort()的方式来创建Sort对象
        Sort sort = Sort.by(Sort.Direction.DESC , "updateTime");

        //https://stackoverflow.com/questions/44848653/pagerequest-constructors-have-been-deprecated
        Pageable pageable = PageRequest.of(0 , size , sort);


        return iBlogRepository.findTop(pageable);
    }

    //Blog 歸檔使用
    //Map<年份 , List<部落格文章>>
    @Override
    public Map<String, List<Blog>> archiveBlog() {

        List<String> years = iBlogRepository.findGroupYear();
        Map<String , List<Blog>> map = new HashMap<>();
        for (String year : years){

            map.put(year,iBlogRepository.findByYear(year));
        }


        return map;
    }

    //共有幾篇Blog文章
    @Override
    public Long countBlog() {
        return iBlogRepository.count();
    }


    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {

        //Add:如果不存在就新增
        if(blog.getId() == null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0); //初始瀏覽次數

        }
        //Edit如果有就編輯
        else {
            //修正編輯時候CreateTime和Views會被洗掉問題 之後看有沒要在修正 2/27
            //其實好像也不用用到Optional好像有點多此一舉,前面也沒用
            Optional<Blog> findBlogById = iBlogRepository.findById(blog.getId());
            blog.setCreateTime(findBlogById.get().getCreateTime());
            blog.setViews(findBlogById.get().getViews());
            blog.setUpdateTime(new Date());

        }
        return iBlogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) throws NotFoundException {

        Optional<Blog> findBlogById = iBlogRepository.findById(id);

        if(!findBlogById.isPresent()){
            throw new NotFoundException("This Blog is not exist");
        }

        Blog targetBlog = findBlogById.get();

        //org.springframework.beans.BeanUtils;  將blog賦值到targetBlog
        BeanUtils.copyProperties(blog,targetBlog);

        return iBlogRepository.save(targetBlog);
    }

    @Override
    @Transactional
    public void deleteBlog(Long id) throws NotFoundException {

        Optional<Blog> findBlogById = iBlogRepository.findById(id);

        if(!findBlogById.isPresent()){
            throw new NotFoundException("This Blog is not exist");
        }

        Blog blog = findBlogById.get();


        iBlogRepository.delete(blog);
    }
}
