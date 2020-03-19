import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class ReadExcelToPic {
    ReadExcelToPic(String s,String b){
        TimeSeriesChart(s,b);
    }

    private static XYDataset getNumberSet(String b) {

        int a=0;double[][] array=new double[3000][3000];
        try{
            //为保证向下兼容性，数据文件采用.xsl格式，如果你的office版本高于2007，即默认文件格式为.xslx，请转存为.xsl格式
            //输入流
            InputStream is = new FileInputStream("D:\\CTGfiles\\username\\CTG\\"+b);
            //文件系统
            POIFSFileSystem fs = new POIFSFileSystem(is);
            //定义工作簿
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            //获取第一个sheet页
            HSSFSheet hssfSheet = wb.getSheetAt(0);


            //遍历行row
            a=hssfSheet.getLastRowNum();
            for(int rowNum = 0; rowNum<=hssfSheet.getLastRowNum();rowNum++){
                //获取每一行
                HSSFRow row = hssfSheet.getRow(rowNum);
                if(row == null){
                    continue;
                }

                //遍历列cell
                for(int cellNum = 0; cellNum<=row.getLastCellNum();cellNum++){
                    //获取每一列
                    HSSFCell cell = row.getCell(cellNum);
                    if(cell == null){
                        continue;
                    }
                    array[rowNum][cellNum]=cell.getNumericCellValue();
                    //System.out.println(" "+(int)cell.getNumericCellValue());
                }
                System.out.println();

            }}catch (IOException e){System.out.println("worrn");}





        XYSeriesCollection tsSet=new XYSeriesCollection();
        XYSeries tp0=new XYSeries("胎心率信号");
        XYSeries tp1=new XYSeries("宫缩信号");
        //XYSeries tp2=new XYSeries("2");

        for (int i=0;i<=a;i++){
            tp0.add(i,array[i][0]);
            tp1.add(i,array[i][1]);
            //tp2.add(i,array[i][2]);
        }
        tsSet.addSeries(tp0);
        tsSet.addSeries(tp1);
        // tsSet.addSeries(tp2);

        return tsSet;


    }

    public static ChartFrame TimeSeriesChart(String s,String b) {
        //创建折线图对象，并命名x，y轴,并格式化
        JFreeChart jfreechart = ChartFactory.createXYLineChart("ID"+s + "  胎心宫缩监测图", "bpm/min", "胎心率信号/宫缩信号", getNumberSet(b), PlotOrientation.VERTICAL, true, true, false);
        jfreechart.getTitle().setFont(new Font("宋体", Font.PLAIN, 12));//标题
        jfreechart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));//图例
        XYPlot plot = jfreechart.getXYPlot();//图表实例化

        //预设值字体格式,重复打了很多遍才想起来可以预设一个，好累哦！
        Font font = new Font("宋体", Font.PLAIN, 12);

        //x轴
        ValueAxis x = (ValueAxis) plot.getDomainAxis(); //实例化x轴
        x.setLabelFont(new Font("宋体", Font.PLAIN, 12));
        x.setTickLabelFont(new Font("宋体", Font.PLAIN, 12));


        //y轴
        NumberAxis y = (NumberAxis) plot.getRangeAxis();//实例化y轴对象
        y.setLabelFont(new Font("宋体", Font.PLAIN, 12));
        y.setTickLabelFont(font);
        //预设y轴格式
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(1);//整数跃度为1
        y.setNumberFormatOverride(nf);//调用y轴预设格式

        //设置图表样式
        XYLineAndShapeRenderer render = (XYLineAndShapeRenderer) plot.getRenderer();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.green);
        plot.setRangeGridlinePaint(Color.green);
        render.setBaseShapesVisible(true);//是否显示节点

        ChartFrame frame = new ChartFrame("胎心宫缩监测系统", jfreechart);
        frame.pack();
        frame.setVisible(true);

        //转存为图片
        FileOutputStream output;
        try {
            output = new FileOutputStream("D:\\CTGfiles\\username\\CTGPic\\" + b + ".jpg");
            ChartUtilities.writeChartAsPNG(output, jfreechart, 450, 400);//图片分辨率
        } catch (Exception e) {
            e.printStackTrace();
        }

        return frame;
    }
}



