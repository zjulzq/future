package zju.lzq.task;

import zju.lzq.future.AbsFuture;
import zju.lzq.future.IndexFuture;
import zju.lzq.future.TreasuryFuture;

public class CaptureTotal {

	/**
	 * args[0]=类名, args[1]=IF/TF, args[2]=日期, args[3]=速度
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			return;
		} else if (args[1].equals("IF")) {
			AbsFuture future = new IndexFuture();
			future.execute(args);
		} else if (args[1].equals("TF")) {
			AbsFuture future = new TreasuryFuture();
			future.execute(args);
		}
	}

}
