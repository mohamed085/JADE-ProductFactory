# JADE-Product Factory

A central agent representing a factory of some type of product wants to sell its products. There are at least three agents representing customers that buy these products one by  one. Each customer will have money to buy the product by requesting it from the factory.  The factory will check for products it can buy at the current time and inform the customer.  The customer will decrease its current balance by the amount it paid for the product. 
The price can change from time to time. If the price changed from the previous time a  customer bought the product then the factory should inform the customer by the new  price.
If the money is not currently ready to the customer, the factory can still give up to two  products to the customer and get the money after that. 
If the customer tried to but more products the factory should inform the customer that it  can only buy more products after paying the money of that product. The factory can make discounts and inform all customers about that discount and its  period. Say valid for one day. 
The factory can receive offersfrom the customers to buy more than one product in a time  but decrease the total cost. For example, if the customer wants to buy 10 products where  the product was priced 1$ then it can pay 9$ only for the 10 products.
