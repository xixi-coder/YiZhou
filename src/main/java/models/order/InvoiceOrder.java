package models.order;

import annotation.TableBind;
import base.models.BaseInvoiceOrder;
import base.models.BaseInvoiceRec;

/**
 * Created by admin on 2017/1/8.
 */
@TableBind(tableName = "dele_invoice_order")
public class InvoiceOrder extends BaseInvoiceOrder<InvoiceOrder> {
    public static InvoiceOrder dao = new InvoiceOrder();
}
