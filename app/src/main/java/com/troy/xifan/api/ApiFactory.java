package com.troy.xifan.api;

import com.troy.xifan.config.Constants;
import com.troy.xifan.http.response.HttpResponseData;
import com.troy.xifan.model.response.DirectMessagesListRes;
import com.troy.xifan.model.response.DirectMessagesRes;
import com.troy.xifan.model.response.NotificationRes;
import com.troy.xifan.model.response.StatusRes;
import com.troy.xifan.model.response.TrendsRes;
import com.troy.xifan.model.response.UserRes;
import java.util.List;
import java.util.Map;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by chenlongfei on 2016/11/19.
 */

public interface ApiFactory {
    //获取token
    @POST
    Observable<HttpResponseData<String>> getAccessToken(@Url String url);

    //获取home timeline
    @GET(Constants.FanFou.HOME_TIMELINE_URL)
    Observable<HttpResponseData<List<StatusRes>>> getHomeTimeline(
            @QueryMap Map<String, String> queryMap);

    //获取指定用户timeline
    @GET(Constants.FanFou.USER_TIMELINE_URL)
    Observable<HttpResponseData<List<StatusRes>>> getUserTimeline(
            @QueryMap Map<String, String> queryMap);

    //随便看看
    @GET(Constants.FanFou.PUBLIC_TIMELIN_URL)
    Observable<HttpResponseData<List<StatusRes>>> getPublicTimeline(
            @QueryMap Map<String, String> queryMap);

    //删除消息
    @POST(Constants.FanFou.DESTROY_STATUSES_URL)
    @Multipart
    Observable<HttpResponseData<StatusRes>> destroyStatus(
            @PartMap Map<String, RequestBody> bodyMap);

    //发消息
    @Multipart
    @POST(Constants.FanFou.UPDATE_STATUSES_URL)
    Observable<HttpResponseData<StatusRes>> updateStatus(@PartMap Map<String, RequestBody> bodyMap);

    //上传图片
    @POST(Constants.FanFou.PHOTOS_UPLOAD_URL)
    @Multipart
    Observable<HttpResponseData<StatusRes>> uploadPhoto(@PartMap Map<String, RequestBody> bodyMap);

    //浏览指定用户的图片
    @GET(Constants.FanFou.USER_PHOTOS_URL)
    Observable<HttpResponseData<List<StatusRes>>> getUserPhotos(
            @QueryMap Map<String, String> queryMap);

    //回复消息
    @GET(Constants.FanFou.MENTIONS_MSG_URL)
    Observable<HttpResponseData<List<StatusRes>>> getMentions(
            @QueryMap Map<String, String> queryMap);

    //私信列表
    @GET(Constants.FanFou.CONVERSATION_LIST_URL)
    Observable<HttpResponseData<List<DirectMessagesListRes>>> getConversationList(
            @QueryMap Map<String, String> queryMap);

    //对话列表
    @GET(Constants.FanFou.CONVERSATION_URL)
    Observable<HttpResponseData<List<DirectMessagesRes>>> getConversation(
            @QueryMap Map<String, String> queryMap);

    //发送私信
    @POST(Constants.FanFou.SEND_DIRECT_MESSAGE_URL)
    @Multipart
    Observable<HttpResponseData<DirectMessagesRes>> sendDirectMessage(
            @PartMap Map<String, RequestBody> bodyMap);

    //收藏消息
    @POST(Constants.FanFou.CREATE_FAVORITES_URL)
    Observable<HttpResponseData<StatusRes>> createFavorite(@Path("id") String id);

    //取消收藏
    @POST(Constants.FanFou.DESTROY_FAVORITES_URL)
    Observable<HttpResponseData<StatusRes>> destroyFavorite(@Path("id") String id);

    //收藏列表
    @GET(Constants.FanFou.FAVORITES_URL)
    Observable<HttpResponseData<List<StatusRes>>> getUserFavorites(
            @QueryMap Map<String, String> queryMap);

    //好友列表
    @GET(Constants.FanFou.USER_FRIENDS_URL)
    Observable<HttpResponseData<List<UserRes>>> getFriends(@QueryMap Map<String, String> queryMap);

    //获取用户信息
    @GET(Constants.FanFou.USER_SHOW_URL)
    Observable<HttpResponseData<UserRes>> getUserInfo(@QueryMap Map<String, String> queryMap);

    //热门话题
    @GET(Constants.FanFou.TRENDS_LIST_URL)
    Observable<HttpResponseData<TrendsRes>> getTrends();

    //搜索全站消息
    @GET(Constants.FanFou.SEARCH_PUBLIC_TIMELINE)
    Observable<HttpResponseData<List<StatusRes>>> searchPublicTimeline(
            @QueryMap Map<String, String> queryMap);

    //搜索用户
    @GET(Constants.FanFou.SEARCH_USER)
    Observable<HttpResponseData<List<StatusRes>>> searchUser(
            @QueryMap Map<String, String> queryMap);

    //搜索指定用户消息
    @GET(Constants.FanFou.SEARCH_USER_TIMELINE)
    Observable<HttpResponseData<List<StatusRes>>> searchUserTimeline(
            @QueryMap Map<String, String> queryMap);

    //获取用户的关注者
    @GET(Constants.FanFou.USER_FOLLOWERS_URL)
    Observable<HttpResponseData<List<UserRes>>> getFollowers(
            @QueryMap Map<String, String> queryMap);

    //添加好友
    @POST(Constants.FanFou.CREATE_FRIENDSHIP_URL)
    @Multipart
    Observable<HttpResponseData<UserRes>> createFriendship(
            @PartMap Map<String, RequestBody> bodyMap);

    //取消关注好友
    @POST(Constants.FanFou.DESTROY_FRIENDSHIP_URL)
    @Multipart
    Observable<HttpResponseData<UserRes>> destoryFriendship(
            @PartMap Map<String, RequestBody> bodyMap);

    //查询两个用户之间是否有follow关系
    @GET(Constants.FanFou.IS_EXIST_FRIENDSHIP_URL)
    Observable<HttpResponseData<String>> isExistFriendship(@QueryMap Map<String, String> queryMap);

    //返回未读的mentions, direct message 以及关注请求数量
    @GET(Constants.FanFou.ACCOUNT_NOTIFICATION)
    Observable<HttpResponseData<NotificationRes>> getNotification();
}