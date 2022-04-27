package com.aizuda.poi.xwpf;

import com.aizuda.common.toolkit.ClassLoaderUtils;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.util.PoitlIOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Docx Word 文档处理类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 * </p>
 * <p>
 * 使用该类需要引入 poi-tl 插件官网 http://deepoove.com/poi-tl/
 * </p>
 *
 * @author 青苗
 * @since 2022-04-27
 */
public class Docx {

    /**
     * 编译渲染模板文件
     *
     * @param templateFilename 模板文件名，例如：templates/word.docx
     * @param model            待渲染对象
     * @return {@link XWPFTemplate}
     */
    public static XWPFTemplate compile(String templateFilename, Object model) {
        return XWPFTemplate.compile(ClassLoaderUtils.getResourceAsStream(templateFilename)).render(model);
    }

    /**
     * 下载编译渲染模板文件
     *
     * @param response         {@link HttpServletResponse}
     * @param templateFilename 模板文件名，例如：templates/word.docx
     * @param model            待渲染对象
     * @param filename         下载文件名
     * @throws IOException
     */
    public static void download(HttpServletResponse response, String templateFilename, Object model, String filename) throws IOException {
        XWPFTemplate template = compile(templateFilename, model);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", String.format("attachment;filename=\"%s\"", filename));
        OutputStream out = response.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(out);
        template.write(bos);
        bos.flush();
        out.flush();
        PoitlIOUtils.closeQuietlyMulti(template, bos, out);
    }
}
