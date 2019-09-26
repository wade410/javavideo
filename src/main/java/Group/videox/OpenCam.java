/**
 * 文件名：javavcCameraTest.java
 * 描述：调用windows平台的摄像头窗口视频
 * 修改时间：2016年6月13日
 * 修改内容：
 */
package Group.videox;
 
import javax.swing.JFrame;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.global.opencv_objdetect;
import org.bytedeco.opencv.opencv_core.IplImage;
 
/**
 * 调用本地摄像头窗口视频
 * @author eguid  
 * @version 2016年6月13日  
 * @see javavcCameraTest  
 * @since  javacv1.2
 */
 
public class OpenCam
{
public static void main(String[] args) throws Exception, InterruptedException
{
    OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);  
    grabber.start();   //开始获取摄像头数据
    CanvasFrame canvas = new CanvasFrame("摄像头");//新建一个窗口
    canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    canvas.setAlwaysOnTop(true);
    
    while(true)
    {
        if(!canvas.isDisplayable())
        {//窗口是否关闭
            grabber.stop();//停止抓取
            System.exit(2);//退出
        }
        canvas.showImage(grabber.grab());//获取摄像头图像并放到窗口上显示， 这里的Frame frame=grabber.grab(); frame是一帧视频图像
 
        Thread.sleep(50);//50毫秒刷新一次图像
    }
}


}
