package com.boco.nscs.core.security;

/**
 * Created by CC on 2017/8/30.
 */

import cn.hutool.core.util.IdUtil;
import com.boco.nscs.core.exception.TokenAuthFailException;
import com.boco.nscs.core.util.ToolUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>jwt token工具类</p>
 * <pre>
 *     jwt的claim里一般包含以下几种数据:
 *         1. iss -- token的发行者
 *         2. sub -- 该JWT所面向的用户
 *         3. aud -- 接收该JWT的一方
 *         4. exp -- token的失效时间
 *         5. nbf -- 在此时间段之前,不会被处理
 *         6. iat -- jwt发布时间
 *         7. jti -- jwt唯一标识,防止重复使用
 * </pre>
 *
 * @author fengshuonan
 * @Date 2017/8/25 10:59
 */
@Component
public class JwtTokenUtil {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    public final static String JWT_IP="IP";
    public final static String JWT_MD5Key = "randomKey";
    public final static String JWT_Scopes="Scopes";
    public final static String HEADER_PREFIX = "Bearer ";

    //签发用户
    public final static String JWT_ISSUser_NSCS="NSCS";
    public final static String JWT_Scopes_Token="TOKEN";
    public final static String JWT_Scopes_Refresh="REFRESH_TOKEN";
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 获取用户名从token中
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token).getSubject();
    }

    /**
     * 获取jwt发布时间
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token).getIssuedAt();
    }

    /**
     * 获取jwt失效时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token).getExpiration();
    }

    public Long getExpirationFromToken(String token) {
        Date exp =  getClaimFromToken(token).getExpiration();
        return exp.getTime();
    }
    /**
     * 获取jwt接收者
     */
    public String getAudienceFromToken(String token) {
        return getClaimFromToken(token).getAudience();
    }

    /**
     * 获取私有的jwt claim
     */
    public String getPrivateClaimFromToken(String token, String key) {
        return getClaimFromToken(token).get(key).toString();
    }

    /**
     * 获取md5 key从token中
     */
    public String getMd5KeyFromToken(String token) {
        return getPrivateClaimFromToken(token, JWT_MD5Key);
    }

    /**
     * 获取jwt的payload部分
     */
    public Claims getClaimFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (Exception e){
            logger.warn("token 非法或已经过期",e);
        }
        return null;
    }

    /**
     * 解析token是否正确,不正确会报异常<br>
     */
    public void parseToken(String token) throws JwtException {
        Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token).getBody();
    }

    /**
     * <pre>
     *  验证token是否失效
     *  true:过期   false:没过期
     * </pre>
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateRefreshToken(String token, String ip){
        return validate(token,ip,"refreshToken");
    }
    public Boolean validateToken(String token,String ip){
        return validate(token,ip,"token");
    }
    //验证token是否有效
    public Boolean validate(String token,String clientIP,String type){
        Claims  claims = getClaimFromToken(token);
            if (claims == null) {
                return false;
            }

        //检查发行人
        if (!JWT_ISSUser_NSCS.equalsIgnoreCase(claims.getIssuer())){
            return false;
        }
        //检查token类型
        String scopes = ToolUtil.toStr(claims.get(JWT_Scopes));
        if (type.equalsIgnoreCase("token")) {
            if (!JWT_Scopes_Token.equals(scopes)) {
                return false;
            }
        }
        else{
            if (!JWT_Scopes_Refresh.equals(scopes)) {
                return false;
            }
        }

        //是否过期
        final Date expiration = claims.getExpiration();
        if(expiration.before(new Date())){
            return false;  //过期
        }

        //验证客户端ip
        String cip = ToolUtil.toStr(claims.get(JWT_IP));
        if (!clientIP.equals(cip)){
            return false;
        }

        return true;
    }

    /**
     * 生成token(通过用户名和签名时候用的随机数)
     */
//    public String generateToken(String userName ,String randomKey,String clientIP) {
//        Map<String, Object> claims = genClaims(userName,randomKey,clientIP);
//        claims.put(JWT_Scopes,JWT_Scopes_Token);
//        Long expire = jwtProperties.getExpiration();
//        return doGenerateToken(claims, userName,expire);
//    }

    public JwtTokenInfo generateToken(String userName ,String clientIP){
        String randomKey = this.getRandomKey();
        Map<String, Object> claims = genClaims(userName,randomKey,clientIP);
        claims.put(JWT_Scopes,JWT_Scopes_Token);
        Long expire = jwtProperties.getExpiration();
        String token= doGenerateToken(claims, userName,expire);
        String refreshToken =generateRefreshToken(token);
        Long expTime = getExpirationFromToken(token);

        JwtTokenInfo jwtToken = new JwtTokenInfo();
        jwtToken.setExpire(expTime);
        jwtToken.setRandomKey(randomKey);
        jwtToken.setRefreshToken(refreshToken);
        jwtToken.setToken(token);
        return  jwtToken;
    }
    //根据刷新token生成新的token
    public JwtTokenInfo refreshNewToken(String refreshToken){
        //todo: 增加只能刷新1次限制

        Claims  claims=getClaimFromToken(refreshToken);
        //重新生成
        String userName =claims.getSubject();
        String clientIP = ToolUtil.toStr(claims.get(JWT_IP));

        return generateToken(userName,clientIP);
    }

    private Map<String, Object> genClaims(String userName ,String randomKey,String clientIP){
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWT_MD5Key, randomKey);
        claims.put(JWT_IP, clientIP);

        return claims;
    }

    //生成刷新用token
    public String generateRefreshToken(String token) {
        Claims  claims=getClaimFromToken(token);

        String userName = claims.getSubject();
        String randomKey = ToolUtil.toStr(claims.get(JWT_MD5Key));
        String clientIP = ToolUtil.toStr(claims.get(JWT_IP));

        //生成新token
        Map<String, Object> newClaims = genClaims(userName,randomKey,clientIP);
        //添加
        newClaims.put(JWT_Scopes,JWT_Scopes_Refresh);  //修改
        newClaims.put("jti", IdUtil.randomUUID());

        //刷新token过期时间 增加30分钟;
        Long expire = jwtProperties.getExpiration()+1800;
        return doGenerateToken(newClaims, userName,expire);
    }

    /**
     * 生成token
     */
    private String doGenerateToken(Map<String, Object> claims, String subject,Long expire) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expire * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(JWT_ISSUser_NSCS)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    /**
     * 获取混淆MD5签名用的随机字符串
     */
    private String getRandomKey() {
        return RandomUtil.randomString(6);
    }

    /**
     * 获取Token信息
     * @param request
     * @return
     */
    public String getRequestToken(HttpServletRequest request){
        //从header中获取token
        String authToken = request.getHeader(jwtProperties.getTokenName());

        //如果header中不存在token，则从参数中获取token
        if(StrUtil.isBlank(authToken)){
            authToken = request.getParameter("token");
        }

        //token为空
        if(StrUtil.isBlank(authToken)){
            return null;
        }
        if (authToken.startsWith(HEADER_PREFIX)){
            //截取掉Bearer 字符串
            authToken = authToken.substring(HEADER_PREFIX.length());
        }
        return authToken;
    }

    public  String getUsernameFromRequest(HttpServletRequest request){
        try {
            String authToken = getRequestToken(request);
            return getUsernameFromToken(authToken);
        }
        catch (Exception ex){
            return  null;
        }
    }
}