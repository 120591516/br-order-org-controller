package br.order.org.controller.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import br.crm.pojo.dict.DictImg;
import br.crm.service.dict.DictImgService;

/**
 * 图片上传Controller
 * 
 * @ClassName: UploadImgController
 * @Description: TODO(图片上传Controller)
 * @author adminis
 * @date 2016年9月13日 下午3:42:11
 *
 */
@Controller
@RequestMapping("/uploadImg")
public class UploadImgController {
	// @Autowired
	// private UploadImgUtils uploadImgUtils;
	private String[] TYPE = { ".jpg", ".jpeg", ".gif", ".png", ".bmp" };

	/**
	 * 图片路径
	 */
	@Value("${baseFolder}")
	private String baseFolder;

	/**
	 * 图片Service
	 */
	@Autowired
	private DictImgService dictImgService;

	/**
	 * 图片上传 @Title: uploadImg @Description: TODO(图片上传) @param @param
	 * request @param @param response @param @return @param @throws Exception
	 * 设定文件 @return Map<String,Object> 返回类型 @throws
	 */
	@RequestMapping("/uploadImg")
	@ResponseBody
	public Map<String, Object> uploadImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		DictImg dictImg = new DictImg();
		Boolean flag = false;
		MultipartHttpServletRequest fileRequest = (MultipartHttpServletRequest) request;
		MultipartFile uploadFile = fileRequest.getFile("file");
		// 图片类型校验
		for (String type : TYPE) {
			// 如果是匹配的类型结尾，则校验成功
			if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), type)) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			map.put("data", "图片类型有误");
			return map;
		}
		flag = false;
		try {
			BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
			if (bufferedImage != null) {
				flag = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (flag) {
			String filename = uploadFile.getOriginalFilename();
			String filePath = getFilePath(filename);
			File file = new File(filePath);
			uploadFile.transferTo(file);
			String urlPath = StringUtils.substringAfter(filePath, "/html");

			String url = StringUtils.replaceChars(urlPath, "\\", "/");
			map.put("url", url);
			long size = uploadFile.getSize();
			map.put("size", size);
			String contentType = StringUtils.substringAfterLast(filename, ".");
			map.put("contentType", contentType);
			String imgName = StringUtils.substringBeforeLast(filename, ".");
			map.put("imgName", imgName);
			dictImg.setImgName(imgName);
			dictImg.setImgLocation(url);
			dictImg.setImgSize(size);
			dictImg.setImgType(contentType);
			dictImg.setImgStatus(0);
			dictImg.setCreateTime(new Date());
			dictImg.setEditTime(dictImg.getCreateTime());
			try {
				Long i = dictImgService.insertImg(dictImg);
				if (null != i && i > 0) {
					map.put("imgId", i);
					return map;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		map.put("data", "添加错误");
		return map;
	}

	/**
	 * 获取图片路径 @Title: getFilePath @Description: TODO(获取图片路径) @param @param
	 * originalFilename @param @return 设定文件 @return String 返回类型 @throws
	 */
	private String getFilePath(String originalFilename) {
		// String realPath =
		// request.getSession().getServletContext().getRealPath("/");
		Date nowDate = new Date();
		// String baseFolder =
		// File.separator+"usr"+File.separator+"local"+File.separator+"nginx"+File.separator+"html"+File.separator+"upload";
		baseFolder = baseFolder.replace("-", File.separator);
		String fileFolder = baseFolder + File.separator + new DateTime(nowDate).toString("yyyy") + File.separator + new DateTime(nowDate).toString("MM") + File.separator + new DateTime(nowDate).toString("dd");
		File file = new File(fileFolder);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		String fileName = new DateTime(nowDate).toString("yyyyMMddHHmmssSSS") + RandomUtils.nextInt(1000, 9999) + "." + StringUtils.substringAfterLast(originalFilename, ".");
		return fileFolder + File.separator + fileName;
	}
}
