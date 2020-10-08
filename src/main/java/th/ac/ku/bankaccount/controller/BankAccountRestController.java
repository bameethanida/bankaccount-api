package th.ac.ku.bankaccount.controller;

import org.springframework.web.bind.annotation.*;
import th.ac.ku.bankaccount.data.BankAccountRepository;
import th.ac.ku.bankaccount.data.TransactionRepository;
import th.ac.ku.bankaccount.model.BankAccount;
import th.ac.ku.bankaccount.model.Transaction;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/bankaccount")

public class BankAccountRestController {

    private BankAccountRepository repository;
    private TransactionRepository transactionRepository;

    public BankAccountRestController(BankAccountRepository repository, TransactionRepository transactionRepository) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/customer/{customerId}")
    public List<BankAccount> getAllCustomerId(@PathVariable int customerId) {
        return repository.findByCustomerId(customerId);
    }

    @GetMapping
    public List<BankAccount> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public BankAccount getOne(@PathVariable int id) {
        try {
            return repository.findById(id).get();
        } catch(NoSuchElementException e){
            return null;
        }
    }

    @PostMapping
    public BankAccount create(@RequestBody BankAccount bankAccount){
        repository.save(bankAccount);
        return repository.findById(bankAccount.getId()).get();
    }

    @PutMapping("/{id}")
    public BankAccount update(@PathVariable int id,
                              @RequestBody BankAccount bankAccount) {
        BankAccount record = repository.findById(id).get();
        record.setBalance(bankAccount.getBalance());
        repository.save(record);
        return record;
    }

    @DeleteMapping("/{id}")
    public BankAccount delete(@PathVariable int id) {
        BankAccount record = repository.findById(id).get();
        repository.deleteById(id);
        return record;
    }

    @PostMapping("/transaction")
    public BankAccount makeTransaction(@RequestBody Transaction transaction) {
        BankAccount bankAccount = repository.findById(transaction.getBankAccountId()).get();
        if (transaction.getTransactionType().equals("Withdraw")) {
            bankAccount.setBalance(bankAccount.getBalance() - transaction.getAmount());
        } else if (transaction.getTransactionType().equals("Deposit")) {
            bankAccount.setBalance(bankAccount.getBalance() + transaction.getAmount());
        }
        repository.save(bankAccount);
        return bankAccount;
    }

}
