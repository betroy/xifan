package com.troy.xifan.model.response;

/**
 * Created by chenlongfei on 2017/1/16.
 */

public class DirectMessagesRes {
    /**
     * id : -SVsfHE_1RU
     * text : asdf
     * sender_id : zengke
     * recipient_id : moon
     * created_at : Wed Nov 02 07:49:55 +0000 2011
     * sender_screen_name : 曾科
     * recipient_screen_name : 穆荣均
     * sender : {"id":"zengke","name":"曾科","screen_name":"曾科","location":"","gender":"","birthday":"","description":"mujixx","profile_image_url":"http://avatar1.fanfou.com/s0/00/bm/pt.jpg?1320216698","profile_image_url_large":"http://avatar1.fanfou.com/l0/00/bm/pt.jpg?1320216698","url":"","protected":true,"followers_count":0,"friends_count":0,"favourites_count":0,"statuses_count":0,"following":true,"notifications":true,"created_at":"Mon
     * Oct 06 11:03:08 +0000 2008","utc_offset":28800}
     * recipient : {"id":"moon","name":"穆荣均","screen_name":"穆荣均","location":"北京","gender":"男","birthday":"","description":"..","profile_image_url":"http://avatar.fanfou.com/s0/00/3e/r4.jpg?1319098444","profile_image_url_large":"http://avatar.fanfou.com/l0/00/3e/r4.jpg?1319098444","url":"","protected":false,"followers_count":0,"friends_count":0,"favourites_count":0,"statuses_count":0,"following":false,"notifications":false,"created_at":"Sat
     * May 12 15:58:58 +0000 2007","utc_offset":28800}
     */

    private String id;
    private String text;
    private String sender_id;
    private String recipient_id;
    private String created_at;
    private String sender_screen_name;
    private String recipient_screen_name;
    private UserRes sender;
    private UserRes recipient;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSender_screen_name() {
        return sender_screen_name;
    }

    public void setSender_screen_name(String sender_screen_name) {
        this.sender_screen_name = sender_screen_name;
    }

    public String getRecipient_screen_name() {
        return recipient_screen_name;
    }

    public void setRecipient_screen_name(String recipient_screen_name) {
        this.recipient_screen_name = recipient_screen_name;
    }

    public UserRes getSender() {
        return sender;
    }

    public void setSender(UserRes sender) {
        this.sender = sender;
    }

    public UserRes getRecipient() {
        return recipient;
    }

    public void setRecipient(UserRes recipient) {
        this.recipient = recipient;
    }
}