package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class VariantWithOrderAnswer implements ModifyRecord, Serializable {
    private ArrayList<Variant> variants;


    public VariantWithOrderAnswer() {
        variants= new ArrayList<>();
    }

    public ArrayList<Variant> getVariants() {
        return variants;
    }

    @Override
    public boolean addRecord() {
        return false;
    }

    @Override
    public boolean delRecord(int indexDel) {
        return false;
    }

    @Override
    public boolean editRecord(int indexEdit) {
        return false;
    }

    @Override
    public String toString() {
        return "VariantWithOrderAnswer{" +
                "variants=" + variants +
                '}';
    }
}
