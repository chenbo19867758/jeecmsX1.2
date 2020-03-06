/**
 *
 */

package com.jeecms.system.job;

import java.util.Calendar;
import java.util.Date;

import com.jeecms.common.constants.SysConstants.TimeUnit;

/**
 * 定时任务工具类
 *
 * @author: tom
 * @date: 2019年5月15日 上午10:51:40
 */
public class SysJobUtil {

        /**
         * 获取 quarz cron表达式 根据执行时间
         *
         * @param jobTime
         *                任务执行时间
         * @Title: createCron
         * @return: String
         */
        public static String createCron(Date jobTime) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(jobTime);
                StringBuffer cronBuff = new StringBuffer();
                cronBuff.append(calendar.get(Calendar.SECOND)).append(" ");
                cronBuff.append(calendar.get(Calendar.MINUTE)).append(" ");
                cronBuff.append(calendar.get(Calendar.HOUR_OF_DAY)).append(" ");
                cronBuff.append(calendar.get(Calendar.DAY_OF_MONTH)).append(" ");
                cronBuff.append(calendar.get(Calendar.MONTH) + 1).append(" ");
                cronBuff.append(" ? ");
                //cronBuff.append(calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.YEAR)).append(" ");
                return cronBuff.toString();
        }

        /**
         * 获取 quarz cron表达式 根据执行时间
         * 
         * @Title: createCron
         * @param jobBeginTime
         *                任务开始时间
         * @param interval
         *                间隔长度
         * @param intervalUnit
         *                间隔单位
         * @return: String
         */
        public static String createCron(Date jobBeginTime, Integer interval, TimeUnit intervalUnit) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(jobBeginTime);
                StringBuffer cronBuff = new StringBuffer();
                cronBuff.append(calendar.get(Calendar.SECOND)).append(" ");
                cronBuff.append(calendar.get(Calendar.MINUTE));
                if (interval != null && TimeUnit.minute.equals(intervalUnit)) {
                        cronBuff.append("/" + interval);
                }
                cronBuff.append(" ").append(calendar.get(Calendar.HOUR_OF_DAY));
                if (interval != null && TimeUnit.hour.equals(intervalUnit)) {
                        cronBuff.append("/" + interval);
                }
                cronBuff.append(" ").append(calendar.get(Calendar.DAY_OF_MONTH));
                if (interval != null && TimeUnit.day.equals(intervalUnit)) {
                        cronBuff.append("/" + interval);
                }
                cronBuff.append(" ").append(calendar.get(Calendar.MONTH) + 1).append(" ");
                cronBuff.append(" ?  ");
                //cronBuff.append(calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.YEAR)).append(" ");
                return cronBuff.toString();
        }

        public static void main(String[] args) {
                Date now = Calendar.getInstance().getTime();
                createCron(now, 5, TimeUnit.minute);
        }
}
