# RetailCoreBankingSystem
Merlion Bank is a new retail bank that will be opening in Singapore soon. 
You have been asked to develop a Retail Core Banking System (RCBS) for Merlion Bank that will consist of 
i) a core backend to be developed with a component-based architecture; and 
ii) multiple retail banking applications to support the business operation of the bank

The current version is able to 
1) create new customer
• For a new customer, teller needs to create a new customer record first before performing any other business activities. 
• Each customer should be uniquely identified with one customer record.

2) open deposit account
• Teller opens a new deposit account for an existing customer.
• A customer can have multiple deposit accounts of different types.
• For this phase, only savings account is required.
• For this phase, you may assume that there is only individual account, i.e., customers cannot open a joint account.

3) issue new and replacement ATM
• Teller issue a new or replacement ATM card to customer.
• For replacement of lost or damaged ATM card, the previous ATM card must be disabled first.
• An ATM card may be associated with one or more deposit accounts.
• Each deposit account may be associated with zero or one ATM card.

4) change PIN for the ATM card 
• Customer change current PIN of the ATM card.

5) enquire available balance
• Customer enquires available balance for a deposit account associated with the ATM card.
• Available balance refers to the balance amount in a deposit account that is available for spending, withdrawal or transfers.
• Ledger balance is equal to the sum of available balance plus any holding balance.
• If there is no holding balance, then ledger balance is equal to available balance.
