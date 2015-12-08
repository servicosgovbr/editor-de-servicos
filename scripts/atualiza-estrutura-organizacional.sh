#!/bin/bash
git add . -A
git stash
git pull --rebase


echo 'Iniciando download lista de órgãos do SIORG'
curl -sH 'Accept-encoding: gzip' "http://estruturaorganizacional.dados.gov.br/doc/estrutura-organizacional/resumida.json?retornarOrgaoEntidadeVinculados;=SIM" > src/main/resources/estrutura-organizacional.json.gz
echo 'Lista de órgãos do SIORG foi atualizada!'


git pull --rebase
git push
git stash pop

