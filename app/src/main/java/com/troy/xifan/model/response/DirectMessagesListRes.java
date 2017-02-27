package com.troy.xifan.model.response;

/**
 * Created by chenlongfei on 2017/1/13.
 */

public class DirectMessagesListRes {

    /**
     * dm : {"id":"-SVsfHE_1RU","text":"asdf","sender_id":"zengke","recipient_id":"moon","created_at":"Wed
     * Nov 02 07:49:55 +0000 2011","sender_screen_name":"曾科","recipient_screen_name":"穆荣均","sender":{"id":"zengke","name":"曾科","screen_name":"曾科","location":"","gender":"","birthday":"","description":"mujixx","profile_image_url":"http://avatar1.fanfou.com/s0/00/bm/pt.jpg?1320216698","profile_image_url_large":"http://avatar1.fanfou.com/l0/00/bm/pt.jpg?1320216698","url":"","protected":true,"followers_count":0,"friends_count":0,"favourites_count":0,"statuses_count":0,"following":true,"notifications":true,"created_at":"Mon
     * Oct 06 11:03:08 +0000 2008","utc_offset":28800},"recipient":{"id":"moon","name":"穆荣均","screen_name":"穆荣均","location":"北京","gender":"男","birthday":"","description":"..","profile_image_url":"http://avatar.fanfou.com/s0/00/3e/r4.jpg?1319098444","profile_image_url_large":"http://avatar.fanfou.com/l0/00/3e/r4.jpg?1319098444","url":"","protected":false,"followers_count":0,"friends_count":0,"favourites_count":0,"statuses_count":0,"following":false,"notifications":false,"created_at":"Sat
     * May 12 15:58:58 +0000 2007","utc_offset":28800}}
     * otherid : zengke
     * msg_num : 11
     * new_conv : true
     */

    private DirectMessagesRes dm;
    private String otherid;
    private int msg_num;
    private boolean new_conv;

    public DirectMessagesRes getDm() {
        return dm;
    }

    public void setDm(DirectMessagesRes dm) {
        this.dm = dm;
    }

    public String getOtherid() {
        return otherid;
    }

    public void setOtherid(String otherid) {
        this.otherid = otherid;
    }

    public int getMsg_num() {
        return msg_num;
    }

    public void setMsg_num(int msg_num) {
        this.msg_num = msg_num;
    }

    public boolean isNew_conv() {
        return new_conv;
    }

    public void setNew_conv(boolean new_conv) {
        this.new_conv = new_conv;
    }
}
