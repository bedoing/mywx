package com.woawi.wx.server.config;

import com.woawi.wx.message.NoMessageRouter;
import com.woawi.wx.server.properties.WxInfo;
import com.woawi.wx.util.WxMpUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ylxia
 * @version 1.0
 * @package com.woawi.server.config
 * @date 15/11/21
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(WxInfo.class)
public class WxConfiguration {

    @Bean
    public WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage(WxInfo info) {
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(info.getAppId()); // 设置微信公众号的appid
        config.setSecret(info.getSecret()); // 设置微信公众号的app corpSecret
        config.setToken(info.getToken()); // 设置微信公众号的token
        config.setAesKey(info.getAesKey()); // 设置微信公众号的EncodingAESKey
        return config;
    }


    @Bean
    public WxMpService wxMpService(WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage) {
        WxMpService wxService = new WxMpServiceImpl();
        wxService.setWxMpConfigStorage(wxMpInMemoryConfigStorage);
        return wxService;
    }


    @Bean
    public WxMpMessageRouter wxMpMessageRouter(WxMpService wxMpService) {
        WxMpMessageRouter wxMpMessageRouter = new WxMpMessageRouter(wxMpService);

        WxMpUtil.text(wxMpMessageRouter, false, "test", "This is a test !!!");
        wxMpMessageRouter.rule().handler(new NoMessageRouter()).end();
        return wxMpMessageRouter;
    }
}