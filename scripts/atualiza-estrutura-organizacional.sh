#!/bin/bash
curl -sH 'Accept-encoding: gzip' "http://estruturaorganizacional.dados.gov.br/doc/estrutura-organizacional/resumida.json?retornarOrgaoEntidadeVinculados;=SIM" > src/main/resources/estrutura-organizacional.json.gz
