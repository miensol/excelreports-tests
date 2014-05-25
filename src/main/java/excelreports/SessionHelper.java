package excelreports;

import com.google.common.collect.Lists;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.concurrent.Callable;

public class SessionHelper {
    private SessionFactory sessionFactory;

    public SessionHelper(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void withinTransaction(final Action<Session> job){
        withinTransaction(new Function<Void, Session>() {
            @Override
            public Void call(Session session) {
                job.call(session);
                return null;
            }
        });
    }

    public <T> T withinTransaction(Function<T, Session> callable) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            T result = callable.call(session);
            transaction.commit();
            transaction = null;
            return result;
        } finally {
            if(transaction != null){
                transaction.rollback();
            }
            if(session != null && session.isOpen()){
                session.close();
            }
        }

    }

    public void saveInTransaction(final Object... entities) {
        withinTransaction(new Action<Session>() {
            @Override
            public void call(Session session) {
                for(Object entity: Lists.newArrayList(entities)){
                    session.save(entity);
                }
            }
        });
    }

    public interface Function<TResult, TArg> {
        TResult call(TArg arg);
    }
    public interface Action<TArg> {
        void call(TArg session);
    }
}
