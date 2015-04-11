Vagrant.configure('2') do |config|

  config.vm.box = 'puppetlabs/centos-7.0-64-puppet'
  config.ssh.forward_agent = true

  config.vm.provider 'virtualbox' do |vb|
    vb.customize ['modifyvm', :id, '--memory', '1024']
  end

  config.vm.define 'app', primary: true do |app|
    app.vm.provision :shell, inline: '/bin/bash /vagrant/scripts/vagrant-bootstrap'
    app.vm.network 'forwarded_port', guest: 80, host: 8081
    app.vm.network 'forwarded_port', guest: 8080, host: 8082
  end

end
