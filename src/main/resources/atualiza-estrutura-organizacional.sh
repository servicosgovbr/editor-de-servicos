#!/bin/bash
curl -sH 'Accept-encoding: gzip' "http://estruturaorganizacional.dados.gov.br/doc/estrutura-organizacional/resumida.json?retornarOrgaoEntidadeVinculados;=SIM" > estrutura-organizacional.json.gz
