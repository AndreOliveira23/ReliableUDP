# ReliableUDP
Implementação de confiabilidade, controle de fluxo/congestionamento sobre o protocolo UDP


## :memo: Descrição básica da lógica de programação usada nos arquivos "UDPClient.java" e "UDPServer.java"
[Essencialmente, por se tratar de um ambiente controlado, é garantido que as mensagens enviadas pelo cliente chegarão ao servidor e vice-versa]

## Atuais versões estáveis: 6 e 7

## Versão 6
A versão 6 basicamente parte dessa ideia e usa o input do cliente para printar no ack, ao invés de printar a mensagem contida no ack enviado pelo servidor.

Isso acontece também porque o ack do servidor não continha a mensagem enviada pelo usuário, mas sim, outra mensagem, que era usada como "código" para indicar qual simulação seria feita.

Por exemplo: Usuário envia mensagem qualquer. Servidor recebe. Em seguida, usa-se um random para decidir a simulação a ser feita. Se random == 1, a simulação escolhida era a de 'cálculo de atraso de propagação', a mensagem enviada pelo servidor continha a keyword "calculando". O cliente recebia a mensagem, e caso a mensagem recebida tivesse essa keyword, o cliente realizava a simulação. 

Esse era o processo aplicado na maioria das simulações. Essencialmente, ao executar e ver os logs, as simulações funcionam, porém, o grupo optou por desenvolver simulações "mais fiéis" ao modelo cliente-servidor real visto em aula. Assim surgiu a versão 7


## Versão 7
A versão 7 separa melhor quais simulações são feitas no lado do cliente, e quais são feitas no lado do servidor, além de usar melhor os ACK's. Nessa versão, as simulações que são feitas do lado do cliente, são executadas, em seguida, o pacote contendo a mensagem é enviado. O ACK retornado pelo servidor contém somente a mensagem do usuário. (Quando se trata de uma simulação de perda de pacote, a mensagem do usuário é substituida por uma mensagem que diz que o pacote se perdeu). Dessa forma, quando o cliente recebe o ACK, ele printa. A fidelidade ao modelo real é alcançada pelo fato de que o ack printado pelo cliente, dessa vez, corresponde ao pacote enviado pelo servidor, o que comprova a comunicação feita partindo do servidor em direção ao cliente

Comentários dentro dos arquivos complementam a explicação

