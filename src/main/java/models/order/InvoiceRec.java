package models.order;

import annotation.TableBind;
import base.models.BaseInvoiceRec;

/**
 * Created by admin on 2017/1/8.
 */
@TableBind(tableName = "dele_invoice_rec")
public class InvoiceRec extends BaseInvoiceRec<InvoiceRec> {
    public static InvoiceRec dao = new InvoiceRec();
}
