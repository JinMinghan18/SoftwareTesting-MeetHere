package com.meethere.service.impl;

import com.meethere.MeetHereApplication;
import com.meethere.dao.NewsDao;
import com.meethere.entity.News;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MeetHereApplication.class)
class NewsServiceImplTest {
    @Mock
    private NewsDao newsDao;
    @InjectMocks
    private NewsServiceImpl newsService;

    @Test
    @DisplayName("返回所有所需新闻信息")
    public void return_all_news_paged_by_page() {
        Pageable pageable= PageRequest.of(0,10);
        when(newsDao.findAll(pageable)).thenReturn(null);
        newsService.findAll(pageable);
        verify(newsDao).findAll(pageable);
    }

    @Test
    @DisplayName("返回一条新闻信息")
    void return_news_by_newsID() {
        int id=1;
        String title="title";
        String content="this is content";
        LocalDateTime ldt=LocalDateTime.now();
        News news=new News(id,title,content,ldt);
        when(newsDao.getOne(id)).thenReturn(news);
        News res= newsService.findById(id);
        assertAll("test find by newsID",()->assertEquals(id,res.getNewsID()),
                ()->assertEquals(title,res.getTitle()),
                ()->assertEquals(content,res.getContent()),
                ()->assertEquals(ldt,res.getTime()));

    }

    @Test
    @DisplayName("新建一条新闻")
    void create_news() {
        int id=1;
        String title="title";
        String content="this is content";
        LocalDateTime ldt=LocalDateTime.now();
        News news=new News(id,title,content,ldt);
        when(newsDao.save(news)).thenReturn(news);
        int res = newsService.create(news);
        assertEquals(id,res);

        verify(newsDao).save(news);
        verifyNoMoreInteractions(newsDao);
    }

    @Test
    @DisplayName("删除一条新闻")
    void del_news_by_newsID() {
        newsService.delById(1002);
        verify(newsDao).deleteById(1002);

        newsService.delById(1003);
        verify(newsDao).deleteById(1003);

        verify(newsDao,times(2)).deleteById(anyInt());
    }

    @Test
    @DisplayName("修改一条新闻")
    void update_news() {
        int id=1;
        String title="title";
        String content="this is content";
        LocalDateTime ldt=LocalDateTime.now();
        News news=new News(id,title,content,ldt);
        when(newsDao.save(news)).thenReturn(news);
        newsService.update(news);

        verify(newsDao).save(news);
        verifyNoMoreInteractions(newsDao);
    }
}
