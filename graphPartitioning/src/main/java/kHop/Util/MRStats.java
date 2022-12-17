package kHop.Util;
/*
 * global counters for this MR job
 * 
 * When the NUMBER_OF_GRAY_NODES_PROCESSED == NUMBER_OF_GRAY_NODES_TOBE_PROCESSED, then we are completed
 * 
 */
public enum MRStats {
	NUMBER_OF_PATTEN_EXTENDED,  // used to keep track of how many patterns of size k is been extended from k-1
	NUMBER_OF_PATTEN,
    NUMBER_REJECTED
}
