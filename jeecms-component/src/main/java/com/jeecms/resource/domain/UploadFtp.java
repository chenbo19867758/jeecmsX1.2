/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.domain;

import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.constants.ContentSecurityConstants;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.upload.UploadUtils;
import com.jeecms.common.util.DesUtil;
import com.jeecms.common.util.MyFileUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.SocketException;
import java.util.List;

/**
 * FTP
 *
 * @author: tom
 * @date: 2019年3月5日 下午4:48:37
 */
@Entity
@Table(name = "jc_sys_ftp")
public class UploadFtp extends AbstractDomain<Integer> implements Serializable {

        private static final Logger log = LoggerFactory.getLogger(UploadFtp.class);

        /**
         * 存储文件
         *
         * @param path
         *                文件路径
         * @param ext
         *                文件格式
         * @param in
         *                输入流
         * @throws IOException
         *                 IOException
         * @Title: storeByExt
         * @return: String
         */
        @Transient
        public String storeByExt(String path, String ext, InputStream in) throws IOException {
                String filename = UploadUtils.generateFilename(path, ext);
                store(filename, in);
                return filename;
        }

        @Transient
        public void storeByExt(String path, InputStream in) throws IOException {
                store(path, in);
        }

        @Transient
        public String storeByFilename(String filename, InputStream in) throws IOException {
                store(filename, in);
                return filename;
        }

        @Transient
        public File retrieve(String name, String fileName) throws IOException {
                String path = System.getProperty("java.io.tmpdir");
                if(StringUtils.isNotBlank(fileName)){
                        File file = new File(path, fileName);
                        file = UploadUtils.getUniqueFile(file);
                        FTPClient ftp = getClient();
                        if (ftp != null) {
                                OutputStream output = new FileOutputStream(file);
                                ftp.retrieveFile(getFtpPath() + name, output);
                                output.close();
                                ftp.logout();
                                ftp.disconnect();
                        }
                        return file; 
                }
                return null;
        }

        /**
         * 删除文件
         *
         * @param fileName
         *                文件路径
         * @Title: deleteFile
         * @return: boolean
         */
        @Transient
        public boolean deleteFile(String fileName) {
                boolean flag = true;
                try {
                        FTPClient ftp = getClient();
                        if (ftp != null && StringUtils.isNotBlank(fileName)) {
                                flag = ftp.deleteFile(fileName);
                        }
                } catch (IOException e) {
                        flag = false;
                        log.error("ftp delete error", e);
                }
                return flag;
        }

        /**
         * 存储文件
         *
         * @param name
         *                文件名
         * @param file
         *                文件
         * @throws IOException
         *                 IOException
         * @Title: restore
         * @return: boolean
         */
        @Transient
        public boolean restore(String name, File file) throws IOException {
                store(name, FileUtils.openInputStream(file));
                file.deleteOnExit();
                return true;
        }

        /**
         * 存储文件夹
         *
         * @param folder
         *                文件夹
         * @param rootPath
         *                根路径
         * @Title: storeByFloder
         * @return: int
         */
        @Transient
        public int storeByFloder(String folder, String rootPath) {
                String fileAbsolutePath;
                String fileRelativePath;
                try {
                        FTPClient ftp = getClient();
                        if (ftp != null) {
                                List<File> files = MyFileUtils.getFiles(new File(folder));
                                for (File file : files) {
                                        String filename = getFtpPath() + file.getName();
                                        String name = FilenameUtils.getName(filename);
                                        String path = FilenameUtils.getFullPath(filename);
                                        fileAbsolutePath = file.getAbsolutePath();
                                        fileRelativePath = fileAbsolutePath.substring(rootPath.length() + 1,
                                                        fileAbsolutePath.indexOf(name));
                                        path += fileRelativePath.replace("\\", WebConstants.SPT);
                                        if (!ftp.changeWorkingDirectory(path)) {
                                                String[] ps = StringUtils.split(path, WebConstants.SPT);
                                                String p = WebConstants.SPT;
                                                ftp.changeWorkingDirectory(p);
                                                for (String s : ps) {
                                                        p += s + WebConstants.SPT;
                                                        if (!ftp.changeWorkingDirectory(p)) {
                                                                ftp.makeDirectory(s);
                                                                ftp.changeWorkingDirectory(p);
                                                        }
                                                }
                                        }
                                        // 解决中文乱码问题
                                        name = new String(name.getBytes(getEncoding()), "iso-8859-1");
                                        if (!file.isFile()) {
                                                ftp.makeDirectory(name);
                                        } else {
                                                InputStream in = new FileInputStream(file.getAbsolutePath());
                                                ftp.storeFile(name, in);
                                                in.close();
                                        }
                                }
                                ftp.logout();
                                ftp.disconnect();
                        }
                        return 0;
                } catch (SocketException e) {
                        log.error("ftp store error", e);
                        return 3;
                } catch (IOException e) {
                        log.error("ftp store error", e);
                        return 4;
                }
        }

        @Transient
        private int store(String remote, InputStream in) {
                try {
                        FTPClient ftp = getClient();
                        if (ftp != null) {
                                //getFtpPath() +
                                String filename =  remote;
                                String name = FilenameUtils.getName(filename);
                                String path = FilenameUtils.getFullPath(filename);
                                if (!ftp.changeWorkingDirectory(path)) {
                                        String[] ps = StringUtils.split(path, WebConstants.SPT);
                                        String p = WebConstants.SPT;
                                        ftp.changeWorkingDirectory(p);
                                        for (String s : ps) {
                                                p += s + WebConstants.SPT;
                                                if (!ftp.changeWorkingDirectory(p)) {
                                                        ftp.makeDirectory(s);
                                                        ftp.changeWorkingDirectory(p);
                                                }
                                        }
                                }
                                ftp.storeFile(name, in);
                                ftp.logout();
                                ftp.disconnect();
                        }
                        return 0;
                } catch (SocketException e) {
                        log.error("ftp store error", e);
                        return 3;
                } catch (IOException e) {
                        log.error("ftp store error", e);
                        return 4;
                } finally {
                        try {
                                if (null != in) {
                                        in.close();
                                }
                        } catch (IOException e) {
                                log.error(e.getMessage());
                        }
                }
        }

        @Transient
        private FTPClient getClient() throws SocketException, IOException {
                FTPClient ftp = new FTPClient();
                ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
                ftp.setDefaultPort(getPort());
                ftp.connect(getIp());
                int reply = ftp.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                        log.error("FTP server refused connection: {}", getIp());
                        ftp.disconnect();
                        return null;
                }
                String password = DesUtil.decrypt(getPassword(), ContentSecurityConstants.DES_KEY,
                                ContentSecurityConstants.DES_IV);
                if (!ftp.login(getUsername(), password)) {
                        log.error("FTP server refused login: {}, user: {}", getIp(), getUsername());
                        ftp.logout();
                        ftp.disconnect();
                        return null;
                }
                ftp.setControlEncoding(getEncoding());
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                ftp.enterLocalPassiveMode();
                return ftp;
        }

        private static final long serialVersionUID = 1L;

        /**
         * 主键
         */
        private Integer id;
        /**
         * 名称
         */
        private String ftpName;
        /**
         * IP
         */
        private String ip;
        /**
         * 端口号
         */
        private Integer port;
        /**
         * 登录名
         */
        private String username;
        /**
         * 登陆密码
         */
        private String password;
        /**
         * 登录密码混淆码
         */
        private String obfuscationCode;
        /**
         * 编码
         */
        private String encoding;
        /**
         * 超时时间
         */
        private Integer timeout;
        /**
         * 路径
         */
        private String ftpPath;
        /**
         * 访问URL
         */
        private String url;

        @Id
        @TableGenerator(name = "jc_sys_ftp", pkColumnValue = "jc_sys_ftp", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_ftp")
        @Override
        public Integer getId() {
                return id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "ftp_name")
        @NotNull
        @Length(max = 150)
        public String getFtpName() {
                return ftpName;
        }

        public void setFtpName(String ftpName) {
                this.ftpName = ftpName;
        }

        @Column(name = "ip")
        @NotNull
        @Length(max = 50)
        public String getIp() {
                return ip;
        }

        public void setIp(String ip) {
                this.ip = ip;
        }

        @Column(name = "port")
        @NotNull
        public Integer getPort() {
                return port;
        }

        public void setPort(Integer port) {
                this.port = port;
        }

        @Column(name = "username")
        @Length(max = 150)
        @NotNull
        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        @Column(name = "password")
        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        @Column(name = "obfuscation_code")
        @Length(max = 150)
        public String getObfuscationCode() {
                return obfuscationCode;
        }

        public void setObfuscationCode(String obfuscationCode) {
                this.obfuscationCode = obfuscationCode;
        }

        @Column(name = "encoding")
        @NotNull
        @Length(max = 20)
        public String getEncoding() {
                return encoding;
        }

        public void setEncoding(String encoding) {
                this.encoding = encoding;
        }

        @Column(name = "timeout")
        public Integer getTimeout() {
                return timeout;
        }

        public void setTimeout(Integer timeout) {
                this.timeout = timeout;
        }

        @Column(name = "ftp_path")
        @Length(max = 255)
        public String getFtpPath() {
                return ftpPath;
        }

        public void setFtpPath(String ftpPath) {
                this.ftpPath = ftpPath;
        }

        @Column(name = "url")
        @NotNull
        @Length(max = 255)
        public String getUrl() {
                return url;
        }

        public void setUrl(String url) {
                this.url = url;
        }

        public UploadFtp() {
                super();
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((encoding == null) ? 0 : encoding.hashCode());
                result = prime * result + ((ftpName == null) ? 0 : ftpName.hashCode());
                result = prime * result + ((ftpPath == null) ? 0 : ftpPath.hashCode());
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((ip == null) ? 0 : ip.hashCode());
                result = prime * result + ((obfuscationCode == null) ? 0 : obfuscationCode.hashCode());
                result = prime * result + ((password == null) ? 0 : password.hashCode());
                result = prime * result + ((port == null) ? 0 : port.hashCode());
                result = prime * result + ((timeout == null) ? 0 : timeout.hashCode());
                result = prime * result + ((url == null) ? 0 : url.hashCode());
                result = prime * result + ((username == null) ? 0 : username.hashCode());
                return result;
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj) {
                        return true;
                }
                if (obj == null) {
                        return false;
                }
                if (getClass() != obj.getClass()) {
                        return false;
                }
                UploadFtp other = (UploadFtp) obj;
                if (encoding == null) {
                        if (other.encoding != null) {
                                return false;
                        }
                } else if (!encoding.equals(other.encoding)) {
                        return false;
                }
                if (ftpName == null) {
                        if (other.ftpName != null) {
                                return false;
                        }
                } else if (!ftpName.equals(other.ftpName)) {
                        return false;
                }
                if (ftpPath == null) {
                        if (other.ftpPath != null) {
                                return false;
                        }
                } else if (!ftpPath.equals(other.ftpPath)) {
                        return false;
                }
                if (id == null) {
                        if (other.id != null) {
                                return false;
                        }
                } else if (!id.equals(other.id)) {
                        return false;
                }
                if (ip == null) {
                        if (other.ip != null) {
                                return false;
                        }
                } else if (!ip.equals(other.ip)) {
                        return false;
                }
                if (obfuscationCode == null) {
                        if (other.obfuscationCode != null) {
                                return false;
                        }
                } else if (!obfuscationCode.equals(other.obfuscationCode)) {
                        return false;
                }
                if (password == null) {
                        if (other.password != null) {
                                return false;
                        }
                } else if (!password.equals(other.password)) {
                        return false;
                }
                if (port == null) {
                        if (other.port != null) {
                                return false;
                        }
                } else if (!port.equals(other.port)) {
                        return false;
                }
                if (timeout == null) {
                        if (other.timeout != null) {
                                return false;
                        }
                } else if (!timeout.equals(other.timeout)) {
                        return false;
                }
                if (url == null) {
                        if (other.url != null) {
                                return false;
                        }
                } else if (!url.equals(other.url)) {
                        return false;
                }
                if (username == null) {
                        if (other.username != null) {
                                return false;
                        }
                } else if (!username.equals(other.username)) {
                        return false;
                }
                return true;
        }

	/**
	 * 复制文件夹以及文件夹下的全部内容
	 * 
	 * @param sourceDir 源文件夹
	 * @param targetDir 目标文件
	 * @throws IOException 异常
	 */
	public void copyDirectiory(String sourceDir, String targetDir) throws IOException {
		FTPClient ftpClient = getClient();
		// 新建目标目录
		if (!existDirectory(targetDir)) {
			createDirectory(targetDir);
		}
		// 获取源文件夹当前下的文件或目录
		// File[] file = (new File(sourceDir)).listFiles();
		FTPFile[] ftpFiles = ftpClient.listFiles(sourceDir);
		for (int i = 0; i < ftpFiles.length; i++) {
			if (ftpFiles[i].isFile()) {
				copyFile(ftpFiles[i].getName(), sourceDir, targetDir);
			} else if (ftpFiles[i].isDirectory()) {
				copyDirectiory(sourceDir + File.separatorChar + ftpFiles[i].getName(), 
						targetDir + File.separatorChar + ftpFiles[i].getName());
			}
		}
	}

	/**
	 * 判断文件夹是否存在
	 * 
	 * @Title: existDirectory
	 * @param targetDir 目标文件夹
	 * @throws IOException 异常
	 * @throws SocketException 异常
	 */
	private boolean existDirectory(String targetDir) throws SocketException, IOException {
		FTPClient ftpClient = getClient();
		boolean flag = false;
		FTPFile[] ftpFileArr = ftpClient.listFiles(targetDir);
		if (ftpFileArr.length > 0) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 创建文件夹
	 * 
	 * @Title: createDirectory
	 * @param targetDir 目标文件夹
	 * @throws IOException 异常
	 */
	private void createDirectory(String targetDir) throws IOException {
		FTPClient ftpClient = getClient();
		ftpClient.makeDirectory(targetDir);
	}

	/**
	 * 复制文件.
	 * @param sourceFileName 源文件名称
	 * @param sourceDir 源文件夹
	 * @param targetDir 目标文件夹
	 * @throws IOException 异常
	 */
	public void copyFile(String sourceFileName, String sourceDir, String targetDir) throws IOException {
		FTPClient ftpClient = getClient();
		ByteArrayInputStream in = null;
		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		try {
			if (!existDirectory(targetDir)) {
				createDirectory(targetDir);
			}
			ftpClient.setBufferSize(1024 * 2);
			// 变更工作路径
			ftpClient.changeWorkingDirectory(sourceDir);
			// 设置以二进制流的方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// 将文件读到内存中
			ftpClient.retrieveFile(new String(sourceFileName.getBytes("GBK"), "iso-8859-1"), fos);
			in = new ByteArrayInputStream(fos.toByteArray());
			if (in != null) {
				ftpClient.changeWorkingDirectory(targetDir);
				ftpClient.storeFile(new String(sourceFileName.getBytes("GBK"), "iso-8859-1"), in);
			}
		} finally {
			// 关闭流
			if (in != null) {
				in.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}


}