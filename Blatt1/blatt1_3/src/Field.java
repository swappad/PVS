public class Field {
    int maxlength;
    int[] arr;

    public Field(int maxlength) {
        this.maxlength = maxlength;
        this.arr = new int[maxlength];
    }

    public void setFieldNumber(int position, int value) throws MissingFieldException, NullPointerException{
        if(position<arr.length && position>0){
            arr[position]=value;
        }else throw new MissingFieldException("Position out of bounce");
    }
    public int getFieldnumber(int position) throws MissingFieldException{
        if(position<arr.length && position>0){
            return arr[position];
        }else throw new MissingFieldException("Position out of bounce");

    }

    @Override
    public String toString(){
        String out="";
        for(int i=0;i<arr.length;i++){
            out += arr[i] + ";";
        }
        return out;
    }
}
