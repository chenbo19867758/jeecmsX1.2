IP白名单配置:
    在application.properties中增加backup.ip-whitelist配置项, 多个IP以逗号分隔
    例如:
        backup.ip-whitelist=127.0.0.1,0:0:0:0:0:0:0:1,192.168.0.174


启动应用:
    WINDOWS:
        双击或在命令行运行start.bat
    LINUX:
        运行start.sh
    参数:
        status	 查看当前运行状态
	例如:
	    start.bat status

停止应用:
    WINDOWS:
        双击或在命令行运行shutdown.bat
    LINUX:
        运行shutdown.sh