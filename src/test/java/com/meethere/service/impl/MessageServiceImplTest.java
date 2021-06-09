package com.meethere.service.impl;

import com.meethere.MeetHereApplication;
import com.meethere.dao.MessageDao;
import com.meethere.entity.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MeetHereApplication.class)
class MessageServiceImplTest {
    @Mock
    private MessageDao messageDao;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    @DisplayName("根据留言ID查找(成功)")
    public void find_message_by_messageID_success(){
        int id=1;
        LocalDateTime ldt=LocalDateTime.now().minusDays(1);
        Message message=new Message(id,"user","this is a leave message", ldt,1);
        when(messageDao.getOne(id)).thenReturn(message);
        Message res=messageService.findById(id);

        assertAll("test message find by id",
                ()->assertEquals(id,res.getMessageID()),
                ()->assertEquals("user",res.getUserID()),
                ()->assertEquals("this is a leave message",res.getContent()),
                ()->assertEquals(ldt,res.getTime()),
                ()->assertEquals(1,res.getState()));

        verify(messageDao).getOne(id);
    }

    @Test
    @DisplayName("根据留言ID查找(失败)")
    public void find_message_by_messageID_fail(){
        int id=1;
        when(messageDao.getOne(id)).thenReturn(null);

        messageService.findById(id);
        verify(messageDao).getOne(id);
    }


    @Test
    @DisplayName("根据留言ID查找留言")
    public void find_message_by_userID() {
        when(messageDao.findAllByUserID(anyString(),any())).thenReturn(null);
        messageService.findByUser(anyString(),any());
        verify(messageDao).findAllByUserID(anyString(),any());
    }

    @Test
    @DisplayName("发布留言")
    public void create_new_message() {
        LocalDateTime ldt=LocalDateTime.now().minusDays(1);
        Message message=new Message();
        message.setMessageID(1);
        message.setContent("this is a new message");
        message.setState(1);
        message.setTime(ldt);
        message.setUserID("user");
        when(messageDao.save(message)).thenReturn(message);
        assertEquals(1,messageService.create(message));

        verify(messageDao).save(message);
        verifyNoMoreInteractions(messageDao);
    }

    @Test
    @DisplayName("根据留言Id删除留言")
    public void del_message_by_messageID() {
        messageService.delById(1);
        verify(messageDao).deleteById(1);

        messageService.delById(2);
        verify(messageDao).deleteById(2);

        verify(messageDao,times(2)).deleteById(anyInt());
        verifyNoMoreInteractions(messageDao);
    }

    @Test
    @DisplayName("修改留言")
    public void update_message() {
        LocalDateTime ldt=LocalDateTime.now().minusDays(1);
        Message message=new Message();
        message.setMessageID(1);
        message.setContent("this is a new message");
        message.setState(1);
        message.setTime(ldt);
        message.setUserID("user");
        when(messageDao.save(message)).thenReturn(message);
        messageService.update(message);

        verify(messageDao).save(message);
        verifyNoMoreInteractions(messageDao);
    }

    @Test
    @DisplayName("通过留言")
    public void confirm_message_success() {
        int id=1;
        LocalDateTime ldt=LocalDateTime.now().minusDays(1);
        Message message=new Message();
        message.setMessageID(id);
        message.setContent("this is a new message");
        message.setState(1);
        message.setTime(ldt);
        message.setUserID("user");
        when(messageDao.findByMessageID(id)).thenReturn(message);

        messageService.confirmMessage(id);
        verify(messageDao).findByMessageID(id);
        verify(messageDao).updateState(2,id);
        verifyNoMoreInteractions(messageDao);
    }

    @Test
    @DisplayName("通过留言失败")
    public void confirm_message_fail(){
        int id=1;
        when(messageDao.findByMessageID(id)).thenReturn(null);

        assertThrows(RuntimeException.class,
                    ()->messageService.confirmMessage(id),
                    "留言不存在"    );
        verify(messageDao).findByMessageID(id);
        verifyNoMoreInteractions(messageDao);
    }

    @Test
    @DisplayName("驳回留言成功")
    public void reject_message_success() {
        int id=1;
        LocalDateTime ldt=LocalDateTime.now().minusDays(1);
        Message message=new Message();
        message.setMessageID(id);
        message.setContent("this is a new message");
        message.setState(1);
        message.setTime(ldt);
        message.setUserID("user");
        when(messageDao.findByMessageID(id)).thenReturn(message);

        messageService.rejectMessage(id);
        verify(messageDao).findByMessageID(id);
        verify(messageDao).updateState(3,id);
        verifyNoMoreInteractions(messageDao);
    }

    @Test
    @DisplayName("驳回留言失败")
    public void reject_message_fail(){
        int id=1;
        when(messageDao.findByMessageID(id)).thenReturn(null);

        assertThrows(RuntimeException.class,
                ()->messageService.rejectMessage(id),
                "留言不存在"    );
        verify(messageDao).findByMessageID(id);
        verifyNoMoreInteractions(messageDao);
    }

    @Test
    @DisplayName("查找待审核留言")
    public void find_wait_state_message() {
        int wait_state=1;
        Pageable message_pageable= PageRequest.of(0,10, Sort.by("time").descending());
        when(messageDao.findAllByState(wait_state,message_pageable)).thenReturn(null);
        messageService.findWaitState(message_pageable);
        verify(messageDao).findAllByState(wait_state,message_pageable);
    }

    @Test
    @DisplayName("查找通过状态留言")
    public void find_pass_state_message() {
        int pass_state=2;
        Pageable message_pageable= PageRequest.of(0,10, Sort.by("time").descending());
        when(messageDao.findAllByState(pass_state,message_pageable)).thenReturn(null);
        messageService.findPassState(message_pageable);
        verify(messageDao).findAllByState(pass_state,message_pageable);
    }
}