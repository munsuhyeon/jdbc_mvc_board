package com.javagyojin.MVCboard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.javagyojin.MVCboard.dto.BDto;
import com.javagyojin.MVCboard.util.Constant;

public class BDao {

	DataSource dataSource;
	JdbcTemplate template;
	
	public BDao() {
		super();
		
		this.template = Constant.template;
		
		/*try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/Oracle11g");
		} catch(Exception e) {
			e.printStackTrace();
		}*/
		
	}
	
	public ArrayList<BDto> list() {
		
		String query = "SELECT * FROM mvc_board ORDER BY bGroup desc, bStep asc";
		return (ArrayList<BDto>)template.query(query, new BeanPropertyRowMapper(BDto.class));
				
		/*ArrayList<BDto> dtos = new ArrayList<BDto>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			String query = "SELECT * FROM mvc_board ORDER BY bGroup desc, bStep asc";
			pstmt = conn.prepareStatement(query);//sql문 실행
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int bId = rs.getInt("bId");
				String bName = rs.getString("bName");
				String bTitle = rs.getString("bTitle");
				String bContent = rs.getString("bContent");
				Timestamp bDate = rs.getTimestamp("bDate");
				int bHit = rs.getInt("bHit");
				int bGroup = rs.getInt("bGroup");
				int bStep = rs.getInt("bStep");
				int bIndent = rs.getInt("bIndent");
				
				BDto dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
				dtos.add(dto);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null ) rs.close();				
				if(pstmt != null ) pstmt.close();
				if(conn != null ) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return dtos;*/
	}
	
	public void write(final String bName,final  String bTitle, final String bContent) {
		
		String query ="INSERT INTO mvc_board(bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) VALUES (mvc_board_seq.nextval, ?, ?, ?, 0, mvc_board_seq.currval, 0, 0)";
		
		this.template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				
				String query ="INSERT INTO mvc_board(bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) VALUES (mvc_board_seq.nextval, ?, ?, ?, 0, mvc_board_seq.currval, 0, 0)";
				PreparedStatement pstmt = con.prepareStatement(query);
				
				pstmt.setString(1, bName); //값이 변할수 있어서 오류가 남
				pstmt.setString(2, bTitle);// 그러면 위에 안변하게 final을 작성해줘야함
				pstmt.setString(3, bContent);
				
				return pstmt;
			}
		});
		
		/*Connection conn = null;
		PreparedStatement pstmt = null;
		
		String query ="INSERT INTO mvc_board(bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) VALUES (mvc_board_seq.nextval, ?, ?, ?, 0, mvc_board_seq.currval, 0, 0)";
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, bName);
			pstmt.setString(2, bTitle);
			pstmt.setString(3, bContent);
			
			int dbFlag = pstmt.executeUpdate();//성공이면 1 반환
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}*/
	}
	
	public BDto contentView(String strId) {
		
		upHit(strId);
		
		String query = "SELECT * FROM mvc_board WHERE bId=" + strId;
		return template.queryForObject(query, new BeanPropertyRowMapper(BDto.class));
		/*BDto dto = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			String query = "SELECT * FROM mvc_board WHERE bId=?";
			pstmt = conn.prepareStatement(query);//sql문 실행
			pstmt.setInt(1, Integer.parseInt(strId));//문자열인 strId를 int로 변환
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int bId = rs.getInt("bId");
				String bName = rs.getString("bName");
				String bTitle = rs.getString("bTitle");
				String bContent = rs.getString("bContent");
				Timestamp bDate = rs.getTimestamp("bDate");
				int bHit = rs.getInt("bHit");
				int bGroup = rs.getInt("bGroup");
				int bStep = rs.getInt("bStep");
				int bIndent = rs.getInt("bIndent");
				
				dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null ) rs.close();				
				if(pstmt != null ) pstmt.close();
				if(conn != null ) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return dto;*/
	}
	
	public void modify(final String bId, final String bName, final String bTitle, final String bContent) {
		  
		String query ="UPDATE mvc_board SET bName=?, bTitle=?, bContent=? WHERE bId=?";
		this.template.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				// TODO Auto-generated method stub
				pstmt.setString(1, bName);
				pstmt.setString(2, bTitle);
				pstmt.setString(3, bContent);
				pstmt.setInt(4, Integer.parseInt(bId));
				
			}
		});
		
		/*Connection conn = null;
		PreparedStatement pstmt = null;
		
		String query ="UPDATE mvc_board SET bName=?, bTitle=?, bContent=? WHERE bId=?";
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, bName);
			pstmt.setString(2, bTitle);
			pstmt.setString(3, bContent);
			pstmt.setInt(4, Integer.parseInt(bId));
			
			int dbFlag = pstmt.executeUpdate();//성공이면 1 반환
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}*/
	}
	
	
	public void delete(final String bId) {
		
		String query ="DELETE FROM mvc_board where bId=?";
		
		this.template.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				// TODO Auto-generated method stub
				pstmt.setInt(1, Integer.parseInt(bId));
			}
		});
		
		/*Connection conn = null;
		PreparedStatement pstmt = null;
		
		String query ="DELETE FROM mvc_board where bId=?";
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(query);
					
			pstmt.setInt(1, Integer.parseInt(bId));
			
			int dbFlag = pstmt.executeUpdate();//성공이면 1 반환
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}*/
		
	}
	
	public BDto reply_view(String strId) {
		
		String query = "SELECT * FROM mvc_board WHERE bId=?";
		
		return template.queryForObject(query, new BeanPropertyRowMapper(BDto.class));
		/*BDto dto = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			String query = "SELECT * FROM mvc_board WHERE bId=?";
			pstmt = conn.prepareStatement(query);//sql문 실행
			pstmt.setInt(1, Integer.parseInt(strId));//문자열인 strId를 int로 변환
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int bId = rs.getInt("bId");
				String bName = rs.getString("bName");
				String bTitle = rs.getString("bTitle");
				String bContent = rs.getString("bContent");
				Timestamp bDate = rs.getTimestamp("bDate");
				int bHit = rs.getInt("bHit");
				int bGroup = rs.getInt("bGroup");
				int bStep = rs.getInt("bStep");
				int bIndent = rs.getInt("bIndent");
				
				dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null ) rs.close();				
				if(pstmt != null ) pstmt.close();
				if(conn != null ) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return dto;*/
		
	}
	
	public void reply(final String bId, final String bName, final String bTitle, final String bContent, final String bGroup, final String bStep, final String bIndent) {
		
		replyShape(bGroup, bStep);//step 값 셋팅 함수
		
		String query ="INSERT INTO mvc_board(bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) VALUES (mvc_board_seq.nextval, ?, ?, ?, 0, ?, ?, ?)";
		
		this.template.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				// TODO Auto-generated method stub
				pstmt.setString(1, bName);
				pstmt.setString(2, bTitle);
				pstmt.setString(3, bContent);
				pstmt.setInt(4, Integer.parseInt(bGroup));
				pstmt.setInt(5, Integer.parseInt(bStep)+1);
				pstmt.setInt(6, Integer.parseInt(bIndent)+1);
				
			}
		});
		
		
		/*Connection conn = null;
		PreparedStatement pstmt = null;
		
		String query ="INSERT INTO mvc_board(bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) VALUES (mvc_board_seq.nextval, ?, ?, ?, 0, ?, ?, ?)";
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, bName);
			pstmt.setString(2, bTitle);
			pstmt.setString(3, bContent);
			pstmt.setInt(4, Integer.parseInt(bGroup));
			pstmt.setInt(5, Integer.parseInt(bStep)+1);
			pstmt.setInt(6, Integer.parseInt(bIndent)+1);
			
			int dbFlag = pstmt.executeUpdate();//성공이면 1 반환
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}*/
	}
	
	public void replyShape(final String strGroup, final String strStep) {
		
		String query ="UPDATE mvc_board SET bStep = bStep + 1 WHERE bGroup = ? and bStep > ?";
		
		this.template.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				// TODO Auto-generated method stub
				pstmt.setInt(1, Integer.parseInt(strGroup));
				pstmt.setInt(2, Integer.parseInt(strStep));			
				
			}
		});
		/*Connection conn = null;
		PreparedStatement pstmt = null;
		
		String query ="UPDATE mvc_board SET bStep = bStep + 1 WHERE bGroup = ? and bStep > ?";
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(query);			
			
			pstmt.setInt(1, Integer.parseInt(strGroup));
			pstmt.setInt(2, Integer.parseInt(strStep));			
			
			int dbFlag = pstmt.executeUpdate();//성공이면 1 반환
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}*/
		
	}
	
	public void upHit(final String strId) {
		
		String query ="UPDATE mvc_board SET bHit = bHit + 1 WHERE bId = ?";
		
		this.template.update(query,new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				// TODO Auto-generated method stub
				pstmt.setInt(1, Integer.parseInt(strId));
			}
		});
		/*Connection conn = null;
		PreparedStatement pstmt = null;
		
		String query ="UPDATE mvc_board SET bHit = bHit + 1 WHERE bId = ?";
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(query);			
			
			pstmt.setInt(1, Integer.parseInt(strId));
			
			
			int dbFlag = pstmt.executeUpdate();//성공이면 1 반환
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}*/	
	}
}
