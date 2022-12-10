package example.spark_first;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.rdd.RDD;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	System.setProperty("hadoop.home.dir", "c:\\hadoop\\winutil\\");
    	SparkConf sparkConf = new SparkConf().setAppName("spark_first").setMaster("local[*]");
    	//SparkContext sc =new SparkContext("local", "test");
    
    	
    	
    	List<Double> doubleList = new ArrayList<>();
        doubleList.add(23.44);
        doubleList.add(26.43);
        doubleList.add(75.35);

        doubleList.add(245.767);

        doubleList.add(398.445);

        doubleList.add(94.72);


        

    	JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

    	JavaRDD<Double> javaRDD = sparkContext.parallelize(doubleList);
 

    	 //map

        JavaRDD<Integer> mappedRDD = javaRDD.map(val -> (int)Math.round(val));


        mappedRDD.collect().forEach(System.out::println);


        //reduce

        int reducedResult = mappedRDD.reduce(Integer::sum);


        System.out.println(reducedResult);


        sparkContext.close();
        

	
}
    
    public static int getInt(Double val)
    {
    	return  (int)Math.round(val);
    }

}
