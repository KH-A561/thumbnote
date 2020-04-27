package net.thumbtack.thumbnote.thumbnoteroot.elastic.daoImpl;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.thumbnote.thumbnoteroot.elastic.dao.NoteElasticDao;
import net.thumbtack.thumbnote.thumbnoteroot.elastic.model.ElasticNote;
import net.thumbtack.thumbnote.thumbnoteroot.elastic.repository.NoteElasticRepository;
import net.thumbtack.thumbnote.thumbnoteroot.model.Account;
import net.thumbtack.thumbnote.thumbnoteroot.model.Notebook;
import net.thumbtack.thumbnote.thumbnoteroot.spring.form.utils.CollectionsUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

@Component
@Slf4j
public class NoteElasticDaoImpl implements NoteElasticDao {
    @Autowired
    private NoteElasticRepository noteElasticRepository;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void saveNote(ElasticNote note) {
        try {
            noteElasticRepository.save(note);
        } catch (DataAccessException e) {
            log.error("Can't insert note {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            noteElasticRepository.deleteById(id);
        } catch (DataAccessException e) {
            log.error("Can't delete note {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Set<ElasticNote> findAllByTagsInsideNotebook(Set<String> tags, Notebook notebook, Account account) {
        try {
            QueryBuilder query = new BoolQueryBuilder().
                                     must(termsQuery("tags", tags)).
                                     filter(boolQuery().must(
                                             termsQuery("notebooksIds", CollectionsUtils.convertArrayToSet(notebook.getId())))).
                                     filter(boolQuery().must(
                                             termQuery("accountId", account.getId())));

            SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query).build();
            return new HashSet<>(elasticsearchTemplate.queryForList(searchQuery, ElasticNote.class));
        } catch (DataAccessException e) {
            log.error("Can't find notes {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Set<ElasticNote> findAllByNameInsideNotebook(String name, Notebook notebook, Account account) {
        try {
            QueryBuilder query = new BoolQueryBuilder().
                                     must(matchQuery("name", name)).
                                     filter(boolQuery().must(
                                            termsQuery("notebooksIds", CollectionsUtils.convertArrayToSet(notebook.getId())))).
                                     filter(boolQuery().must(
                                            termQuery("accountId", account.getId())));

            SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query).build();
            return new HashSet<>(elasticsearchTemplate.queryForList(searchQuery, ElasticNote.class));
        } catch (DataAccessException e) {
            log.error("Can't find notes {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Set<ElasticNote> findAllByTextInsideNotebook(String text, Notebook notebook, Account account) {
        try {
            QueryBuilder query = new BoolQueryBuilder().
                                     must(matchQuery("text", text)).
                                     filter(boolQuery().must(
                                             termsQuery("notebooksIds", CollectionsUtils.convertArrayToSet(notebook.getId())))).
                                     filter(boolQuery().must(
                                             termQuery("accountId", account.getId())));

            SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query).build();
            return new HashSet<>(elasticsearchTemplate.queryForList(searchQuery, ElasticNote.class));
        } catch (DataAccessException e) {
            log.error("Can't find notes {}", e.getMessage());
            //TODO: ServerException type
            throw new IllegalArgumentException();
        }
    }
}
