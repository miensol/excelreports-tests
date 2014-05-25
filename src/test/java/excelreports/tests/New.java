package excelreports.tests;

import excelreports.Product;
import excelreports.SessionHelper;
import excelreports.Transaction;
import excelreports.TransactionItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionImpl;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class New {
    private static SessionFactory _factory;
    private static Configuration cfg;

    public static SessionHelper sessionHelper() {
        return new SessionHelper(sessionFactory());
    }

    private synchronized static SessionFactory sessionFactory() {
        if(_factory == null){
            cfg = new Configuration();
            cfg.configure();
            ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties())
                    .build();
            cfg.addAnnotatedClass(Transaction.class);
            cfg.addAnnotatedClass(TransactionItem.class);

            _factory = cfg.buildSessionFactory(registry);
        }
        return _factory;
    }

    public static Transaction transaction(int branchId) {
        return new Transaction(branchId);
    }

    public static Product product() {
        return productNamed("product");
    }

    public static Product productNamed(String name) {
        return new Product(name, 1);
    }
    public static Product productThatCosts(double prize) {
        return new Product("product", prize);
    }

    public static void setupTestDatabase() {
        SessionHelper sessionHelper = New.sessionHelper();
        sessionHelper.withinTransaction(new SessionHelper.Action<Session>() {
            @Override
            public void call(Session session) {
                SessionImpl sessionImpl = (SessionImpl) session;
                new SchemaExport(cfg, sessionImpl.connection()).execute(true,true, false, false);
            }
        });
    }

}
