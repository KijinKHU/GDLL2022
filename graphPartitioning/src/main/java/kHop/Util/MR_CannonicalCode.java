package kHop.Util;
import java.util.*;
import java.io.*;

public class MR_CannonicalCode implements java.io.Serializable {

	public int v1id,v2id,v1label,v2label,elabel;

	public MR_CannonicalCode(){}
	public MR_CannonicalCode(int id1, int id2, int l1, int e,int l2)
	{

		v1id = id1;
		v2id = id2;
		v1label = l1;
		v2label = l2;
		elabel = e;
		
	}
	public boolean lessthan(MR_CannonicalCode c2)
	{
		boolean is_fwd=(this.v1id<this.v2id);
    	boolean rhs_is_fwd=(c2.v1id<c2.v2id);

    if(!is_fwd && rhs_is_fwd)
      return true;

    if(!is_fwd && !rhs_is_fwd && this.v2id<c2.v2id)
      return true;

    if(!is_fwd && !rhs_is_fwd && this.v2id==c2.v2id && this.elabel<c2.elabel)
      return true;

    if(is_fwd && rhs_is_fwd && this.v1id>c2.v1id)
      return true;

    if(is_fwd && rhs_is_fwd && this.v1id==c2.v1id && this.v1label<c2.v1label)
      return true;

    if(is_fwd && rhs_is_fwd && this.v1id==c2.v1id && this.v1label==c2.v1label && this.elabel < c2.elabel)
      return true;

    if(is_fwd && rhs_is_fwd && this.v1id==c2.v1id && this.v1label==c2.v1label && this.elabel == c2.elabel && this.v2label<c2.v2label)
      return true;

    return false;

	}
	public String getCan_code_()
	{
		String can_cod = new String(Integer.toString(this.v1id));
		can_cod = can_cod +"_"+Integer.toString(this.v2id);
		can_cod = can_cod +"_"+Integer.toString(this.v1label);
		can_cod = can_cod +"_"+Integer.toString(this.elabel);
		can_cod = can_cod +"_"+Integer.toString(this.v2label);
		//can_cod = can_cod +";";
		//System.out.println(can_cod);
		return can_cod;
	}
	public String getCan_code_size1()
	{
		String can_cod = new String(Integer.toString(this.v1id));
		can_cod = can_cod +"_"+Integer.toString(this.v2id);
		can_cod = can_cod +"_"+Integer.toString(this.v1label);
		can_cod = can_cod +"_"+Integer.toString(this.elabel);
		can_cod = can_cod +"_"+Integer.toString(this.v2label);
		can_cod = can_cod +";";
		//System.out.println(can_cod);
		return can_cod;
	}

	public int get_elabel()
	{
		return this.elabel;
	}
	
}
