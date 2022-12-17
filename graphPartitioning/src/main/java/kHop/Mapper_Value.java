package kHop;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;




public class Mapper_Value {
	Node_information value;


	public Mapper_Value(Node_information node) {
		this.value = node;
	}

	public BytesWritable toBytesWritable() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this.value);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			System.out.println("Mapper at ToByteWritable: exception " + e.getMessage());
			e.printStackTrace();
		}
		return new BytesWritable(baos.toByteArray());
	}
	
	public MapWritable toMapWritable()
	{
		IntWritable sup = new IntWritable(this.value.getSelf().getNode().getId());
		
		ByteArrayOutputStream baos= new ByteArrayOutputStream();
		try
			{
			
			ObjectOutputStream oos= new ObjectOutputStream(baos);
			oos.writeObject(this.value);
        	oos.flush();
				oos.close();
		}
		catch(IOException e)
			{
				e.printStackTrace();
			}
			
		MapWritable m = new MapWritable();
		m.put(sup,new BytesWritable(baos.toByteArray()));
		
		return m;
	}

}
