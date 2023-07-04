// You will need to design and create your own Service classes from scratch.
// You should refer to prior mini-project lab examples and course material for guidance.
package Service;

import DAO.AccountDao;
import Model.Account;

public class AccountService{
    private AccountDao accountDao;

    public AccountService(){
        accountDao = new AccountDao();
    }

    public AccountService(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    public Account insertAccount(Account account){
        return accountDao.insertAccount(account);
    }

    public Account loginAccount(Account account){
        return accountDao.loginAccount(account);
    }
}