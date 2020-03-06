package com.jeecms.common.file;

import com.jeecms.common.image.ImageUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileFilter;
import java.sql.Timestamp;
import java.util.*;

import static com.jeecms.common.constants.WebConstants.SPT;

/**
 * 文件包装类
 *
 * @author: tom
 * @date: 2018年12月25日 上午9:44:09
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class FileWrap {
        /**
         * 可编辑的后缀名
         */
        public static final String[] EDITABLE_EXT = new String[]{"html", "htm", "css", "js", "txt"};
        private static final String FILE_SUFFIX_JPEG = "jpeg";
        private static final String FILE_SUFFIX_JPG = "jpg";
        private static final String FILE_SUFFIX_PNG = "png";
        private static final String FILE_SUFFIX_GIF = "gif";
        private static final String FILE_SUFFIX_HTM = "htm";
        private static final String FILE_SUFFIX_HTML = "html";
        private static final String FILE_SUFFIX_TXT = "txt";
        private static final String FILE_SUFFIX_SQL = "sql";
        private File file;
        private String rootPath;
        private FileFilter filter;
        private List<FileWrap> child;
        /**
         * 文件名
         */
        private String filename;

        /**
         * 文件路径
         */
        private String name;
        /**
         * 文件大小
         */
        private long size;
        /**
         * 文件所属路径
         */
        private String path;

        /**
         * 最后修改时间
         */
        private long lastModified;
        /**
         * 最后修改时间
         */
        private Date lastModifiedDate;
        private Boolean valid;

        /**
         * 构造器
         *
         * @param file 待包装的文件
         */
        public FileWrap(File file) {
                this(file, null);
        }

        /**
         * 构造器
         *
         * @param file     待包装的文件
         * @param rootPath 根路径，用于计算相对路径
         */
        public FileWrap(File file, String rootPath) {
                this(file, rootPath, null);
        }

        /**
         * 构造器
         *
         * @param file     待包装的文件
         * @param rootPath 根路径，用于计算相对路径
         * @param filter   文件过滤器，应用于所有子文件
         */
        public FileWrap(File file, String rootPath, FileFilter filter) {
                this.file = file;
                this.rootPath = rootPath;
                this.filter = filter;
        }

        /**
         * 构造器
         *
         * @param file     待包装的文件
         * @param rootPath 根路径，用于计算相对路径
         */
        public FileWrap(File file, String rootPath, FileFilter filter, Boolean valid) {
                this.file = file;
                this.rootPath = rootPath;
                this.filter = filter;
                this.valid = valid;
        }

        /**
         * 是否允许编辑
         *
         * @param ext 文件扩展名
         * @return
         */
        public static boolean editableExt(String ext) {
                ext = ext.toLowerCase(Locale.ENGLISH);
                for (String s : EDITABLE_EXT) {
                        if (s.equals(ext)) {
                                return true;
                        }
                }
                return false;
        }

        /**
         * 获得完整文件名，根据根路径(rootPath)。
         *
         * @return
         */
        public String getName() {
                String path = file.getAbsolutePath();
                String relPath = path.substring(rootPath.length());
                // tomcat8 rootPath会加上/结尾
                if (!relPath.startsWith(File.separator)) {
                        relPath = File.separator + relPath;
                }
                this.name = relPath.replace(File.separator, SPT);
                return this.name;
        }

        /**
         * 获得文件路径，不包含文件名的路径。
         *
         * @return
         */
        public String getPath() {
                String name = getName();
                this.path = name.substring(0, name.lastIndexOf('/'));
                return this.path;
        }

        /**
         * 获得文件名，不包含路径的文件名。 如没有指定名称，则返回文件自身的名称。
         *
         * @return
         */
        public String getFilename() {
                this.filename = filename != null ? filename : file.getName();
                return this.filename;
        }

        /**
         * 扩展名
         *
         * @return
         */
        public String getExtension() {
                return FilenameUtils.getExtension(file.getName());
        }

        /**
         * 最后修改时间。长整型(long)。
         *
         * @return
         */
        public long getLastModified() {
                this.lastModified = file.lastModified();
                return this.lastModified;
        }

        /**
         * 最后修改时间。日期型(Timestamp)。
         *
         * @return
         */
        public Date getLastModifiedDate() {
                this.lastModifiedDate = new Timestamp((file.lastModified()));
                return this.lastModifiedDate;
        }

        /**
         * 文件大小，单位KB。
         *
         * @return
         */
        public long getSize() {
                this.size = file.length() / 1024 + 1;
                return this.size;
        }

        /**
         * 获得文件的图标名称
         * <ul>
         * <li>directory = folder</li>
         * <li>jpg,jpeg = jpg</li>
         * <li>gif = gif</li>
         * <li>html,htm = html</li>
         * <li>swf = swf</li>
         * <li>txt = txt</li>
         * <li>其他 = unknow</li>
         * </ul>
         *
         * @return
         */
        public String getIco() {
                if (file.isDirectory()) {
                        return "folder";
                }
                String ext = getExtension();
                if (FILE_SUFFIX_JPEG.equalsIgnoreCase(ext) || FILE_SUFFIX_JPG.equalsIgnoreCase(ext)) {
                        return "jpg";
                } else if (FILE_SUFFIX_PNG.equalsIgnoreCase(ext)) {
                        return "png";
                } else if (FILE_SUFFIX_GIF.equalsIgnoreCase(ext)) {
                        return "gif";
                } else if (FILE_SUFFIX_HTM.equalsIgnoreCase(ext) || FILE_SUFFIX_HTML.equalsIgnoreCase(ext)) {
                        return "html";
                } else if (FILE_SUFFIX_TXT.equalsIgnoreCase(ext)) {
                        return "txt";
                } else if (FILE_SUFFIX_SQL.equalsIgnoreCase(ext)) {
                        return "sql";
                } else {
                        return "unknow";
                }
        }

        /**
         * 获得下级目录 如果没有指定下级目录，则返回文件夹自身的下级文件，并运用FileFilter。
         *
         * @return
         */
        public List<FileWrap> getChild() {
                if (this.child != null) {
                        return this.child;
                }
                File[] files;
                if (filter == null) {
                        files = getFile().listFiles();
                } else {
                        files = getFile().listFiles(filter);
                }
                List<FileWrap> list = new ArrayList<FileWrap>();
                if (files != null) {
                        Arrays.sort(files, new FileComparator());
                        for (File f : files) {
                                FileWrap fw = new FileWrap(f, rootPath, filter);
                                list.add(fw);
                        }
                }
                return list;
        }

        /**
         * 获得被包装的文件
         *
         * @return
         */
        public File getFile() {
                return this.file;
        }

        /**
         * 是否图片
         *
         * @return
         */
        public boolean isImage() {
                if (isDirectory()) {
                        return false;
                }
                String ext = getExtension();
                return ImageUtils.isValidImageExt(ext);
        }

        /**
         * 是否可编辑
         *
         * @return
         */
        public boolean isEditable() {
                if (isDirectory()) {
                        return false;
                }
                String ext = getExtension();
                for (String s : EDITABLE_EXT) {
                        if (s.equalsIgnoreCase(ext)) {
                                return true;
                        }
                }
                return false;
        }

        /**
         * 是否目录
         *
         * @return
         */
        public boolean isDirectory() {
                return file.isDirectory();
        }

        /**
         * 是否文件
         *
         * @return
         */
        public boolean isFile() {
                return file.isFile();
        }

        /**
         * 设置待包装的文件
         *
         * @param file 文件
         */
        public void setFile(File file) {
                this.file = file;
        }

        /**
         * 指定名称
         *
         * @param filename 文件名
         */
        public void setFilename(String filename) {
                this.filename = filename;
        }

        /**
         * 指定下级目录
         *
         * @param child 子节点
         */
        public void setChild(List<FileWrap> child) {
                this.child = child;
        }

        public Boolean getValid() {
                return valid;
        }

        public void setValid(Boolean valid) {
                this.valid = valid;
        }

        /**
         * 文件比较器，文件夹靠前排。
         */
        public static class FileComparator implements Comparator<File> {
                @Override
                public int compare(File o1, File o2) {
                        if (o1.isDirectory() && !o2.isDirectory()) {
                                return -1;
                        } else if (!o1.isDirectory() && o2.isDirectory()) {
                                return 1;
                        } else {
                                return o1.compareTo(o2);
                        }
                }
        }
}
