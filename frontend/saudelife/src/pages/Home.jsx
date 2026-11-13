import React, { useEffect, useState } from 'react';
import api from '../services/api';

export default function Principal() {
  const [usuarios, setUsuarios] = useState([]);
  const [erro, setErro] = useState('');

  useEffect(() => {
    // Fazendo a requisição para o Spring Boot (http://localhost:8080/usuarios)
    api.get('/usuarios')
      .then(response => {
        setUsuarios(response.data);
      })
      .catch(err => {
        console.error("Erro ao buscar usuários:", err);
        setErro("Não foi possível conectar ao backend. Verifique se o Spring Boot está rodando.");
      });
  }, []);

  return (
    <div style={{ padding: '30px', fontFamily: 'Segoe UI, Tahoma, Geneva, Verdana, sans-serif' }}>
      <h1>Saúde Life — Painel Principal</h1>
      <hr />
      
      <h3>Usuários cadastrados no Banco de Dados:</h3>

      {erro && <p style={{ color: 'red' }}>{erro}</p>}

      <ul>
        {usuarios.length > 0 ? (
          usuarios.map(user => (
            <li key={user.id} style={{ marginBottom: '10px' }}>
              <strong>{user.nome}</strong> — <em>{user.email}</em> (Objetivo: {user.objetivo || 'Não definido'})
            </li>
          ))
        ) : (
          <p>Nenhum usuário encontrado ou carregando...</p>
        )}
      </ul>
    </div>
  );
}