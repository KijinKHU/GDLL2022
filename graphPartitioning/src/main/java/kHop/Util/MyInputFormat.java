package kHop.Util;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MultiFileInputFormat;
import org.apache.hadoop.mapred.MultiFileSplit;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;

public class MyInputFormat extends MultiFileInputFormat<NullWritable, Text> {

	@Override
	public RecordReader<NullWritable, Text> getRecordReader(InputSplit split, JobConf job, Reporter reporter)
			throws IOException {
		return new FilepathRecordReader(job, (MultiFileSplit) split);
	}

	public static class FilepathRecordReader implements RecordReader<NullWritable, Text> {

		private MultiFileSplit split;
		private FileSystem fs;
		private int count = 0;
		private Path[] paths;
		private boolean process = false;
		private long offset; // total offset read so far;
		private long totLength;

		public FilepathRecordReader(Configuration conf, MultiFileSplit split) throws IOException {

			this.split = split;
			fs = FileSystem.get(conf);
			this.paths = split.getPaths();
		}

		public void close() throws IOException {
		}

		public long getPos() throws IOException {
			return process ? 1 : 0;
		}

		public float getProgress() throws IOException {
			return process ? 1.0f : 0.0f;
		}

		public boolean next(NullWritable key, Text value) throws IOException {

			if (count >= split.getNumPaths())
				return false;

			if (!process) {
				Path file = paths[count];
				Text filepath = new Text(file.toString());
				value.set(filepath);
				count++;
				// process=true;
				return true;
			}

			return false;
		}

		public Text createValue() {
			return new Text();
		}

		public NullWritable createKey() {
			return NullWritable.get();
		}

	}

}
