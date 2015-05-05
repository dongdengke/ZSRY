package cn.edu.bjtu.zsry.engine;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;
import cn.edu.bjtu.zsry.bean.UpdateInfo;

/**
 * 更新信息的解析类
 * 
 * @author dong
 * 
 */
public class UpdateInfoParse {
	/**
	 * 返回更新信息的业务对象
	 * 
	 * @param in
	 *            输入流
	 * @return null 解析失败,解析成功返回updateinfo
	 */
	public static UpdateInfo getUpdateInfo(InputStream in) {
		UpdateInfo updateInfo = new UpdateInfo();
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(in, "utf-8");
			int type = parser.getEventType();
			while (type != XmlPullParser.END_DOCUMENT) {
				if (type == parser.START_TAG) {
					if ("version".equals(parser.getName())) {
						String version = parser.nextText();
						updateInfo.setVersion(version);
					} else if ("description".equals(parser.getName())) {
						String description = parser.nextText();
						updateInfo.setDescription(description);
					} else if ("aplurl".equals(parser.getName())) {
						String aplurl = parser.nextText();
						updateInfo.setAplurl(aplurl);
					}
				}
				type = parser.next();
			}
			return updateInfo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
