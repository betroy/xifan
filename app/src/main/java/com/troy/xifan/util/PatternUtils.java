package com.troy.xifan.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.widget.TextView;
import com.troy.xifan.App;
import com.troy.xifan.R;
import com.troy.xifan.config.Constants;
import com.troy.xifan.view.ClickLinkMovementMethod;
import com.troy.xifan.view.VerticalImageSpan;
import com.troy.xifan.view.XiFanClickableSpan;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * Created by chenlongfei on 2016/11/25.
 */

public final class PatternUtils {
    // #话题#
    public static final String REGEX_TOPIC = "#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#";
    // [表情]
    public static final String REGEX_EMOTION = "\\[(\\S+?)\\]";
    // url
    public static final String REGEX_URL =
            "http[s]?://[a-zA-Z0-9+&@#/%?=~_\\\\-|!:,\\\\.;]*[a-zA-Z0-9+&@#/%=~_|]";
    // @人
    public static final String REGEX_AT = "@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}";
    //user link
    public static final String REGEX_USER_LINK =
            "@<a href=\"http://fanfou.com/(.*?)\" class=\"former\">(.*?)</a>";

    public static final Pattern PATTERN_TOPIC = compile(REGEX_TOPIC);
    public static final Pattern PATTERN_URL = compile(REGEX_URL);
    public static final Pattern PATTERN_AT = compile(REGEX_AT);

    public static final String SCHEME_TOPIC =
            Constants.Router.SCHEME + Constants.Router.TREND_STATUSES + "?keyword=";
    public static final String SCHEME_URL =
            Constants.Router.SCHEME + Constants.Router.BROWSER + "?url=";
    public static final String SCHEME_AT =
            Constants.Router.SCHEME + Constants.Router.PROFILE + "?userid=";

    private PatternUtils() {
    }

    public static String extractToken(String regex, String responseData) {
        Pattern pattern = compile(regex);
        Matcher matcher = pattern.matcher(responseData);
        if (matcher.find() && matcher.groupCount() >= 1) {
            return matcher.group(1);
        } else {
            throw new RuntimeException("Can't extract token,responseData is" + responseData);
        }
    }

    //将@用户提取出来
    private static Map<String, String> findAtUser(String sourceText) {
        Map<String, String> userMap = new HashMap<>();
        Pattern pattern = Pattern.compile(REGEX_USER_LINK);
        Matcher matcher = pattern.matcher(sourceText);
        while (matcher.find()) {
            String userId = matcher.group(1);
            String userName = matcher.group(2);
            userMap.put(userName, userId);
        }
        return userMap;
    }

    private static void linkifyUsers(Spannable spannable, final Map<String, String> userMap) {
        Linkify.MatchFilter filter = new Linkify.MatchFilter() {
            @Override
            public final boolean acceptMatch(final CharSequence s, final int start, final int end) {
                String name = s.subSequence(start + 1, end).toString().trim();
                return userMap.containsKey(name);
            }
        };
        Linkify.TransformFilter transformFilter = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher matcher, String value) {
                String userName = value.subSequence(1, value.length()).toString().trim();
                String userId = userMap.get(userName);
                return userId;
            }
        };

        Linkify.addLinks(spannable, PATTERN_AT, SCHEME_AT, filter, transformFilter);
    }

    private static void linkifyTopic(Spannable spannable) {
        Linkify.TransformFilter transformFilter = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher matcher, String value) {
                return value.replace("#", "");
            }
        };
        Linkify.addLinks(spannable, PATTERN_TOPIC, SCHEME_TOPIC, null, transformFilter);
    }

    private static void linkifyUrl(Spannable spannable) {
        Linkify.TransformFilter transformFilter = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher matcher, String value) {
                return value;
            }
        };
        Linkify.addLinks(spannable, PATTERN_URL, SCHEME_URL, null, transformFilter);
    }

    /**
     * 格式化微博文本
     * 感谢 http://melodyxxx.com/2016/12/04/use_spannablestring_format_weibo/
     *
     * @param context 上下文
     * @param source 源文本
     * @param textView 目标 TextView
     * @return SpannableStringBuilder
     */
    public static SpannableStringBuilder formatWeiBoContent(Context context, String source,
            TextView textView) {

        // 获取到 TextView 的文字大小，后面的 ImageSpan 需要用到该值
        int textSize = (int) textView.getTextSize();

        // 若要部分 SpannableString 可点击，需要如下设置
        textView.setMovementMethod(ClickLinkMovementMethod.getInstance());
        textView.setFocusable(false);
        textView.setClickable(false);
        textView.setLongClickable(false);

        String content = Html.fromHtml(source).toString();
        // 将要格式化的 String 构建成一个 SpannableStringBuilder
        final SpannableStringBuilder value = new SpannableStringBuilder(content);
        Map<String, String> userMap = findAtUser(source);

        // 使用正则匹配@用户
        linkifyUsers(value, userMap);
        // 使用正则匹配话题
        linkifyTopic(value);
        // 使用正则匹配链接
        linkifyUrl(value);

        // 获取上面到所有 addLinks 后的匹配部分(这里一个匹配项被封装成了一个 URLSpan 对象)
        URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);

        // 遍历所有的 URLSpan
        for (final URLSpan urlSpan : urlSpans) {
            // 自定义的匹配部分的点击效果
            XiFanClickableSpan clickSpan = new XiFanClickableSpan(urlSpan);
            // 话题
            if (urlSpan.getURL().startsWith(SCHEME_TOPIC)) {
                int start = value.getSpanStart(urlSpan);
                int end = value.getSpanEnd(urlSpan);
                value.removeSpan(urlSpan);
                // 格式化话题部分文本
                value.setSpan(clickSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            // @用户
            if (urlSpan.getURL().startsWith(SCHEME_AT)) {
                int start = value.getSpanStart(urlSpan);
                int end = value.getSpanEnd(urlSpan);
                value.removeSpan(urlSpan);
                // 格式化@用户部分文本
                value.setSpan(clickSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            // 链接
            if (urlSpan.getURL().startsWith(SCHEME_URL)) {
                int start = value.getSpanStart(urlSpan);
                int end = value.getSpanEnd(urlSpan);
                value.removeSpan(urlSpan);
                SpannableStringBuilder urlSpannableString =
                        getUrlTextSpannableString(context, urlSpan.getURL(), textSize);
                value.replace(start, end, urlSpannableString);
                // 格式化链接部分文本
                value.setSpan(clickSpan, start, start + urlSpannableString.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return value;
    }

    private static SpannableStringBuilder getUrlTextSpannableString(Context context, String source,
            int size) {
        SpannableStringBuilder builder = new SpannableStringBuilder(source);
        String prefix = " ";
        builder.replace(0, prefix.length(), prefix);
        Drawable drawable = context.getResources().getDrawable(R.mipmap.ic_status_link);
        drawable.setBounds(0, 0, size, size);
        builder.setSpan(new VerticalImageSpan(drawable), prefix.length(), source.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(App.getInstance().getString(R.string.text_url_link));
        return builder;
    }
}
