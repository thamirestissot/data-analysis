package com.thamirestissot.factory;

import com.thamirestissot.model.Customer;
import com.thamirestissot.model.Sale;
import com.thamirestissot.model.Salesman;
import org.junit.Test;

import static com.thamirestissot.factory.ParserFactory.processLine;
import static org.junit.Assert.assertEquals;

public class ParserFactoryTest {

    @Test
    public void testProcessLineValidSalesman() {
        Salesman salesman = (Salesman) processLine("001ç1234567891234çPedroç50000");

        assertEquals("1234567891234", salesman.getCpf());
        assertEquals("Pedro", salesman.getName());
        assertEquals(50000, salesman.getSalary(), 0);
    }

    @Test(expected = NumberFormatException.class)
    public void testProcessLineInvalidSalesman() {
        Salesman salesman = (Salesman) processLine("001ç1234567891234çPedroç50da000");
    }

    @Test
    public void testProcessLineValidSale() {
        Sale sale = (Sale) processLine("003ç10ç[1-10-100,2-30-2.50]çPedro");

        assertEquals(10, sale.getId());
        assertEquals("Pedro", sale.getSalesmanName());
        assertEquals(10, sale.getItems().get(0).getQuantity());
        assertEquals(30, sale.getItems().get(1).getQuantity());
        assertEquals(1, sale.getItems().get(0).getItem().getId());
        assertEquals(2, sale.getItems().get(1).getItem().getId());
        assertEquals(100.0, sale.getItems().get(0).getItem().getPrice(), 1);
        assertEquals(2.5, sale.getItems().get(1).getItem().getPrice(), 1);

    }

    @Test(expected = NumberFormatException.class)
    public void testProcessLineInvalidSale() {
        Sale sale = (Sale) processLine("003ç10ç2-3t0-2çPedro");
    }

    @Test
    public void testProcessLineValidCustomer() {
        Customer customer = (Customer) processLine("002ç2345675434544345çJose da SilvaçRural");

        assertEquals("2345675434544345", customer.getCnpj());
        assertEquals("Jose da Silva", customer.getName());
        assertEquals("Rural", customer.getBusinessArea());

    }
}