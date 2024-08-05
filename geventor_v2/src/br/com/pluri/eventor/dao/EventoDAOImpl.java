package br.com.pluri.eventor.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class EventoDAOImpl{
	

    public int isExistInscritoInEvento(Long idEven) throws SQLException {
    	Connection con = null;
    	con = DBConnection.getConnection();
        CallableStatement callableStatement = con.prepareCall("{call IS_EXIST_INSCRITO_IN_EVENTO(?,?)}");
        callableStatement.setLong(1, idEven);
        callableStatement.registerOutParameter(2, Types.INTEGER);
        int result = callableStatement.getInt(2);
        callableStatement.close();
        con.close();
        return result;
    }

}
