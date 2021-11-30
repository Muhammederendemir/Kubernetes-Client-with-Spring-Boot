package com.muhammederendemir.kubernetesclient.controller;

import org.springframework.web.bind.annotation.*;


import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;

import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/kubernetes")
public class KubernetesClientController {

    public static CoreV1Api api;

    public KubernetesClientController() throws IOException {
        this.api=configuration();
    }

    public CoreV1Api configuration() throws IOException {
        String kubeConfigPath = "./kube-config-file/config";

        ApiClient client = ClientBuilder
                .kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath)))
                .build();

        Configuration.setDefaultApiClient(client);

        return new CoreV1Api();


    }

    @GetMapping("/healtyCheck")
    public String healtyCheck() {

        return "healty";

    }


    @GetMapping("/pods")
    public List<String> getPods() throws ApiException, IOException {

        V1PodList list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        List<String> pods = new ArrayList<>();

        for (V1Pod item : list.getItems()) {
            pods.add(item.getMetadata().getName());
            System.out.println(item.getMetadata().getName());
        }
        return pods;
    }

    @PostMapping("/create/namespace")
    public String createNameSpace(@RequestBody String namespaceName) throws ApiException, IOException {

        V1Namespace ns = new V1Namespace().metadata(new V1ObjectMeta().name(namespaceName));
        V1Namespace createdNamespace=api.createNamespace(ns,null,null,null);

        System.out.println(createdNamespace.getMetadata().getName());
        return createdNamespace.getMetadata().getName();
    }

    @GetMapping("/namespaces")
    public List<String> getNamespaces() throws IOException, ApiException {
        V1NamespaceList listNamespace = api.listNamespace(null, null, null, null, null, 0, null, null, null,null);

        List<String> namespaces= new ArrayList<>();
        for (V1Namespace item : listNamespace.getItems()) {
            namespaces.add(item.getMetadata().getName());
            System.out.println(item.getMetadata().getName());
        }
        return namespaces;
    }


}
