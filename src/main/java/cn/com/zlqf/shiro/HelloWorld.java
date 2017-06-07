package cn.com.zlqf.shiro;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import junit.framework.Assert;

public class HelloWorld {
	
	@Test
	public void testHelloWorld() {
		IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro-realm.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
		try {
			subject.login(token);
		} catch (AuthenticationException e) {
			System.out.println("身份认证失败:" + e.getClass().getName());
		}
		Assert.assertEquals(true, subject.isAuthenticated());
		subject.logout();
	}
	
	@Test
	public void testHasRole() {
		IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro-permission.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
		try {
			subject.login(token);
		} catch (AuthenticationException e) {
			System.out.println("身份认证失败:" + e.getClass().getName());
			return;
		}
		
		System.out.println("登录成功");
		Session session = subject.getSession();
		System.out.println(session.getClass().getName());
		/*
		try {
			res1();
			res2();
			res3();
		} catch (Exception e) {
			System.out.println("没有权限");
		}
		*/
	}
	
	@RequiresPermissions("user:delete")
	public void res1() {
		System.out.println("访问资源1");
	}
	@RequiresPermissions("user:delete")
	public void res2() {
		System.out.println("访问资源2");
	}
	@RequiresPermissions("user:delete")
	public void res3() {
		System.out.println("访问资源3");
	}
	
	@Test
	public void testBase64() throws Exception {
		String str = "Hello";
		String encodeToString = Base64.encodeToString(str.getBytes());
		System.out.println(encodeToString);
		String decodeToString = Base64.decodeToString(encodeToString);
		System.out.println(decodeToString);
		
		byte[] bytes = CodecSupport.toBytes("你好", "UTF-8");
		String strUTF8 = new String(bytes,"UTF-8");
		System.out.println(strUTF8);
	}
	
	@Test
	public void testMD5() {
		String str = "123456";
		String salt = "123";
		String md5 = new Md5Hash(str,salt).toString();
		System.out.println(md5);
	}
}
