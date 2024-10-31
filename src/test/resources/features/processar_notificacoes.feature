# language: pt

Funcionalidade: Processar Notificação
  Como um usuário da API
  Eu quero criar, listar, atualizar, obter e deletar notificações
  Para gerenciar as notificações no sistema

  Contexto:
    Dado que o endpoint "/notificacoes" está disponível

  Cenário: Obter uma notificação existente por ID
    Dado que existe uma notificação com ID 1
    Quando eu faço uma requisição GET para "/notificacoes/1"
    Então o sistema retorna a notificação com status 200

  Cenário: Tentar obter uma notificação inexistente por ID
    Dado que não existe uma notificação com ID 999
    Quando eu faço uma requisição GET para "/notificacoes/999"
    Então o sistema retorna status 404 com mensagem "Notificação não encontrada"

  Cenário: Criar uma nova notificação
    Dado que eu tenho os dados válidos para uma nova notificação
      | campo             | valor               |
      | tipo              | Alerta              |
      | descricao         | Nova notificação    |
      | emailDestinatario | usuario@example.com |
      | dataEnvio         | 2023-10-31          |
    Quando eu faço uma requisição POST para "/notificacoes" com os dados acima
    Então o sistema cria a notificação e retorna status 201 com a localização da nova notificação

  Cenário: Atualizar uma notificação existente
    Dado que existe uma notificação com ID 1
    E eu tenho os dados atualizados para a notificação
      | campo             | valor                    |
      | tipo              | Aviso                    |
      | descricao         | Notificação atualizada   |
      | emailDestinatario | novo_usuario@example.com |
      | dataEnvio         | 2023-11-01               |
    Quando eu faço uma requisição PUT para "/notificacoes/1" com os dados acima
    Então o sistema atualiza a notificação e retorna status 200

  Cenário: Tentar atualizar uma notificação inexistente
    Dado que não existe uma notificação com ID 999
    Quando eu faço uma requisição PUT para "/notificacoes/999" com dados quaisquer
    Então o sistema retorna status 404 com mensagem "Notificação não encontrada"

  Cenário: Deletar uma notificação existente
    Dado que existe uma notificação com ID 1
    Quando eu faço uma requisição DELETE para "/notificacoes/1"
    Então o sistema deleta a notificação e retorna status 200

  Cenário: Tentar deletar uma notificação inexistente
    Dado que não existe uma notificação com ID 999
    Quando eu faço uma requisição DELETE para "/notificacoes/999" com dados quaisquer
    Então o sistema retorna status 404 com mensagem "Notificação não encontrada"
